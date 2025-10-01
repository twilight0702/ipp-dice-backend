package com.ippclub.ippdicebackend.exception;

import com.ippclub.ippdicebackend.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<?> handleBusinessConflictException(BusinessConflictException ex) {
        // 409状态码，表示业务逻辑冲突错误
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.error(409, ex.getMessage()));
    }

    // 处理资源不存在异常
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.error(404, ex.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherExceptions(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", 5000);
        body.put("message", "服务器内部错误: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}