package stu.fzy.dynamicauthorization.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException{
    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED.value(), "authentication failure");
    }
    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), message);
    }
}
