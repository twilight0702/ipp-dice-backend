package com.ippclub.ippdicebackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用响应结果封装类
 * 用于统一API接口返回格式，包含状态码、消息和数据
 *
 * @param <T> 数据类型
 */
@Data
@AllArgsConstructor
public class Response<T> {
    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 默认构造函数
     */
    public Response() {
    }

    /**
     * 成功响应，无数据
     *
     * @param <T> 数据类型
     * @return Response实例
     */
    public static <T> Response<T> success() {
        return new Response<>(200, "success", null);
    }

    /**
     * 成功响应，带数据
     *
     * @param data 数据
     * @param <T> 数据类型
     * @return Response实例
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(200, "success", data);
    }

    /**
     * 成功响应，自定义消息
     *
     * @param message 消息
     * @param data 数据
     * @param <T> 数据类型
     * @return Response实例
     */
    public static <T> Response<T> success(String message, T data) {
        return new Response<>(200, message, data);
    }

    /**
     * 失败响应，使用默认错误码和消息
     *
     * @param <T> 数据类型
     * @return Response实例
     */
    public static <T> Response<T> error() {
        return new Response<>(500, "error", null);
    }

    /**
     * 失败响应，自定义消息
     *
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Response实例
     */
    public static <T> Response<T> error(String message) {
        return new Response<>(500, message, null);
    }

    /**
     * 失败响应，自定义错误码和消息
     *
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Response实例
     */
    public static <T> Response<T> error(Integer code, String message) {
        return new Response<>(code, message, null);
    }
}
