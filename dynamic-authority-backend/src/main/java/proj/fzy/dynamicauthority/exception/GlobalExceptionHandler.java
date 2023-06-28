package proj.fzy.dynamicauthority.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import proj.fzy.dynamicauthority.model.dto.CommonResponse;

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
    public CommonResponse<Void> authorizationExceptionHandler(AuthorizationException e) {
        return CommonResponse.simpleResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public CommonResponse<Void> notFoundExceptionHandler(NotFoundException e) {
        return CommonResponse.simpleResponse(e.getCode(), e.getMessage());
    }
}
