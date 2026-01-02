package com.nebula.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Unified API response wrapper
 * Encapsulates all controller method return values
 *
 * @param <T> Type of data being returned
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Response code (200 for success, others for errors)
     */
    private Integer code;

    /**
     * Response message
     */
    private String message;

    /**
     * Response data
     */
    private T data;

    /**
     * Response timestamp
     */
    private String timestamp;

    /**
     * Create a success response without data
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * Create a success response with data
     *
     * @param data Response data
     */
    public static <T> Result<T> success(T data) {
        return success(data, "Operation successful");
    }

    /**
     * Create a success response with data and custom message
     *
     * @param data    Response data
     * @param message Custom message
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        result.setTimestamp(LocalDateTime.now().format(FORMATTER));
        return result;
    }

    /**
     * Create an error response
     *
     * @param message Error message
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * Create an error response with custom code
     *
     * @param code    Error code
     * @param message Error message
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        result.setTimestamp(LocalDateTime.now().format(FORMATTER));
        return result;
    }

    /**
     * Create a validation error response
     *
     * @param message Validation error message
     */
    public static <T> Result<T> validationError(String message) {
        return error(400, message);
    }

    /**
     * Create a not found error response
     *
     * @param message Not found error message
     */
    public static <T> Result<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * Create an unauthorized error response
     *
     * @param message Unauthorized error message
     */
    public static <T> Result<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * Create a forbidden error response
     *
     * @param message Forbidden error message
     */
    public static <T> Result<T> forbidden(String message) {
        return error(403, message);
    }
}
