package proj.fzy.dynamicauthority.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationException extends BaseException{
    public AuthorizationException() {
        super(HttpStatus.FORBIDDEN.value(), "Authorization Failure");
    }

    public AuthorizationException(Integer code, String message) {
        super(code, message);
    }
}
