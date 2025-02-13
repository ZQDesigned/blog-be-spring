# 个人博客后端服务

这是一个基于 Spring Boot 3.x 开发的个人博客后端服务，提供了博客文章、项目展示、分类标签等功能的 RESTful API。

## 技术栈

- **核心框架：** Spring Boot 3.2.2
- **安全框架：** Spring Security + JWT
- **持久层：** Spring Data JPA
- **缓存：** Redis + Caffeine
- **数据库：** MySQL 8.x
- **API文档：** 基于 Markdown 的静态文档
- **其他：**
  - Lombok：简化 Java 代码
  - Jackson：JSON 处理
  - Commons Lang3：工具类库
  - Validation：参数校验

## 主要功能

### 博客文章管理
- 文章的 CRUD 操作
- 文章分类和标签管理
- 文章访问统计
- 支持 Markdown 格式
- 文章列表分页和排序

### 项目展示
- 项目信息管理
- GitHub 仓库链接
- 在线演示链接
- 技术栈展示
- 项目特性说明

### 分类和标签
- 分类的 CRUD 操作
- 标签的 CRUD 操作
- 文章数量统计
- 按分类/标签筛选文章

### 系统功能
- JWT 认证
- 管理员登录
- 密码修改
- 访问日志记录
- 多级缓存
- 跨域支持

## 特性

- **RESTful API：** 符合 REST 规范的 API 设计
- **统一响应：** 统一的响应格式和错误处理
- **参数校验：** 完善的请求参数验证
- **缓存机制：** 两级缓存（Redis + Caffeine）提升性能
- **安全性：** 基于 JWT 的认证和授权
- **软删除：** 支持数据软删除
- **数据关联：** 完善的数据关联关系处理
- **异常处理：** 统一的异常处理机制
- **跨域支持：** 可配置的跨域请求支持

## 项目结构

```
src/main/java/com/yiyunnetwork/blogbe/
├── config          // 配置类
├── controller      // 控制器
├── dto            // 数据传输对象
├── entity         // 实体类
├── repository     // 数据访问层
├── service        // 服务层
│   └── impl      // 服务实现
├── security       // 安全相关
├── util           // 工具类
├── common         // 公共类
└── exception      // 异常处理
```

## 环境要求

- JDK 17+
- MySQL 8.x
- Redis 6.x

## 开发说明

1. **配置文件**
   - 主配置文件：`application.properties`
   - 示例配置：`application.properties.example`
   - 请复制示例配置并根据实际情况修改

2. **数据库**
   - 自动创建数据库和表结构
   - 默认创建管理员账号

3. **缓存配置**
   - Redis：分布式缓存
   - Caffeine：本地缓存

4. **安全配置**
   - JWT 密钥配置
   - 跨域配置
   - 接口权限配置

## 待办事项

- [ ] 添加单元测试
- [ ] 添加接口文档自动生成
- [ ] 优化缓存策略
- [ ] 添加数据库索引优化
- [ ] 添加接口限流
- [ ] 添加操作日志
- [ ] 添加数据备份功能

## 开源协议

[MIT License](LICENSE) 