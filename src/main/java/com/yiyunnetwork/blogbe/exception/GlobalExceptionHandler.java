package com.yiyunnetwork.blogbe.exception;

import com.yiyunnetwork.blogbe.common.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public Result<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return Result.error(404, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
                
        log.debug("参数验证失败: {}", message);
        return Result.error(400, "参数验证失败: " + message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
                
        String message = String.join("; ", errors);
        log.debug("参数验证失败: {}", message);
        return Result.error(400, "参数验证失败: " + message);
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
                
        log.debug("参数绑定失败: {}", message);
        return Result.error(400, "参数绑定失败: " + message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.debug("请求参数格式错误: {}", e.getMessage());
        String message = e.getMessage();
        if (message != null && message.contains("Required request body is missing")) {
            return Result.error(400, "请求体不能为空");
        }
        // 提取更有用的错误信息
        String detailedMessage = "请求参数格式错误";
        if (message != null) {
            if (message.contains("JSON parse error")) {
                detailedMessage += ": JSON解析失败";
                // 尝试提取具体的字段信息
                if (message.contains("Unrecognized field")) {
                    String field = message.substring(message.indexOf("\"") + 1, message.lastIndexOf("\""));
                    detailedMessage += String.format("，未知字段 '%s'", field);
                }
            } else if (message.contains("Required request body")) {
                detailedMessage = "请求体不能为空";
            }
        }
        return Result.error(400, detailedMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Result<?> handleBadCredentialsException(BadCredentialsException e) {
        return Result.error(401, "用户名或密码错误");
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException e) {
        return Result.error(401, "认证失败: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(403, "无权限访问");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result<?> handleExpiredJwtException(ExpiredJwtException e) {
        return Result.error(401, "令牌已过期");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public Result<?> handleMalformedJwtException(MalformedJwtException e) {
        return Result.error(401, "无效的令牌格式");
    }

    @ExceptionHandler(SignatureException.class)
    public Result<?> handleSignatureException(SignatureException e) {
        return Result.error(401, "令牌签名无效");
    }

    @ExceptionHandler(JwtException.class)
    public Result<?> handleJwtException(JwtException e) {
        return Result.error(401, "令牌验证失败");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常", e);
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            message = "系统发生未知错误";
        }
        return Result.error(500, "系统异常: " + message);
    }
} 