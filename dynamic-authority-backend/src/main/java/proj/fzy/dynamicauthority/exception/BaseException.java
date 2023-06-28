package proj.fzy.dynamicauthority.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private Integer code;
    private String message;
}
