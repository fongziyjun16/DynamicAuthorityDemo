package stu.fzy.dynamicauthorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private Integer code;
    private String message;
}
