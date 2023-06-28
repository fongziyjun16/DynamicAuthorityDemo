package proj.fzy.dynamicauthority.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException{
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND.value(), "not found");
    }

    public NotFoundException(Integer code, String message) {
        super(code, message);
    }
}
