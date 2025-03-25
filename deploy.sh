#!/bin/zsh

# 导入配置
if [ ! -f "./deploy.config.sh" ]; then
  echo "错误: 配置文件 deploy.config.sh 不存在"
  echo "请复制 deploy.config.example.sh 为 deploy.config.sh 并修改配置"
  exit 1
fi

source ./deploy.config.sh

# 检查 Java 环境
if ! command -v java &> /dev/null; then
  echo "错误: Java 未安装，请先安装 JDK。"
  exit 1
fi

# 执行 Gradle 构建命令
echo "正在执行构建命令：./gradlew bootJar..."
./gradlew bootJar

if [ $? -ne 0 ]; then
  echo "构建失败，请检查 Gradle 配置和代码。"
  exit 1
fi

# 检查构建输出目录是否存在
if [ ! -d "$LOCAL_PATH/$BUILD_DIR" ]; then
  echo "错误: 构建输出目录 $BUILD_DIR 不存在，请检查构建脚本。"
  exit 1
fi

# 获取构建产物（JAR 包）
JAR_FILE=$(ls -t "$LOCAL_PATH/$BUILD_DIR"/*.jar | head -1)
if [ -z "$JAR_FILE" ]; then
  echo "错误: 未找到构建产物（JAR 包）"
  exit 1
fi

JAR_NAME=$(basename "$JAR_FILE")
echo "找到 JAR 包：$JAR_NAME"

# 根据认证方式构建 SSH 命令
if [ "$SSH_METHOD" = "key" ]; then
  SSH_CMD="ssh -i \"$SSH_KEY\" -p \"$REMOTE_PORT\""
  SCP_CMD="scp -i \"$SSH_KEY\" -P \"$REMOTE_PORT\""
else
  # 使用 sshpass 进行密码认证
  if ! command -v sshpass &> /dev/null; then
    echo "错误: 使用密码认证需要安装 sshpass"
    echo "Mac: brew install sshpass"
    echo "Linux: apt-get install sshpass 或 yum install sshpass"
    exit 1
  fi
  SSH_CMD="sshpass -p \"$REMOTE_PASSWORD\" ssh -p \"$REMOTE_PORT\""
  SCP_CMD="sshpass -p \"$REMOTE_PASSWORD\" scp -P \"$REMOTE_PORT\""
fi

# 检查远程服务器是否为宝塔环境
IS_BT_ENV=$(eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'command -v bt &> /dev/null && echo yes || echo no'")

# 如果是宝塔环境，先停止服务
if [ "$IS_BT_ENV" = "yes" ]; then
  echo "检测到宝塔环境，正在停止服务..."
  STOP_OUTPUT=$(eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'java-service $PROJECT_NAME stop'")
  echo "$STOP_OUTPUT"
  sleep 3
else
  echo "非宝塔环境，将使用普通的进程管理..."
  # 查找并关闭旧的 Java 进程
  eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'pid=\$(ps -ef | grep \"$JAR_NAME\" | grep -v grep | awk \"{print \\\$2}\") && if [ ! -z \"\$pid\" ]; then kill \$pid; fi'"
  sleep 3
fi

# 删除远程服务器上的旧 JAR 包
echo "正在删除远程服务器上的旧 JAR 包..."
eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'rm -f \"$REMOTE_PATH\"/*.jar'"

# 上传 JAR 包到远程服务器
echo "正在上传 $JAR_NAME 到远程服务器..."
eval "$SCP_CMD \"$JAR_FILE\" \"$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH/$JAR_NAME\""

if [ $? -ne 0 ]; then
  echo "文件上传失败，请检查网络连接和认证配置。"
  exit 1
fi

echo "JAR 包上传成功！"

# 确保远程目录和 JAR 包有正确的权限
eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'chmod 755 \"$REMOTE_PATH\" && chmod 644 \"$REMOTE_PATH/$JAR_NAME\"'"

# 如果是宝塔环境，启动服务
if [ "$IS_BT_ENV" = "yes" ]; then
  echo "正在启动服务..."
  START_OUTPUT=$(eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'java-service $PROJECT_NAME start'")
  echo "$START_OUTPUT"
  
  # 根据启动输出判断服务状态
  if [[ $START_OUTPUT == *"启动成功"* ]] || [[ $START_OUTPUT == *"项目运行中"* ]]; then
    echo "服务已成功启动！"
  else
    echo "警告: 服务可能未正常启动，请检查服务状态。"
  fi
else
  # 非宝塔环境，直接启动 JAR
  echo "正在启动应用..."
  eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'cd \"$REMOTE_PATH\" && nohup java -jar $JAR_NAME > nohup.out 2>&1 &'"
  sleep 3
  
  # 检查进程是否启动
  PROCESS_CHECK=$(eval "$SSH_CMD \"$REMOTE_USER@$REMOTE_HOST\" 'ps -ef | grep \"$JAR_NAME\" | grep -v grep'")
  if [ ! -z "$PROCESS_CHECK" ]; then
    echo "应用已成功启动！"
  else
    echo "警告: 应用可能未正常启动，请检查日志。"
  fi
fi

echo "部署完成！"
