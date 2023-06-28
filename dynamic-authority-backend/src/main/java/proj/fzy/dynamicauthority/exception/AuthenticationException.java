package proj.fzy.dynamicauthority.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException{
    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED.value(), "Authentication Failure");
    }

    public AuthenticationException(Integer code, String message) {
        super(code, message);
    }
}
