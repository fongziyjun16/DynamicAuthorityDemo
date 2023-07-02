package stu.fzy.dynamicauthorization.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonResponse<Void> exceptionHandler(Exception e) {
        e.printStackTrace();
        return CommonResponse.simpleResponse(400500, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public CommonResponse<Void> authenticationExceptionHandler(AuthenticationException e) {
        return CommonResponse.simpleResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public CommonResponse<Void> authorizationExceptionExceptionHandler(AuthorizationException e) {
        return CommonResponse.simpleResponse(e.getCode(), e.getMessage());
    }
}