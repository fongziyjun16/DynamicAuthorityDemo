package stu.fzy.dynamicauthorization.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BaseException{
    public AuthorizationException() {
        super(HttpStatus.FORBIDDEN.value(), "authorization failure");
    }
    public AuthorizationException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }
}
