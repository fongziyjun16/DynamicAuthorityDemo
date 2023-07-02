package stu.fzy.dynamicauthorization.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CommonResponse <T>{
    private Integer code;
    private String message;
    private T data;

    public static CommonResponse<Void> simpleResponse(Integer code, String message) {
        return CommonResponse.<Void>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static CommonResponse<Void> simpleSuccess() {
        return CommonResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .build();
    }

    public static <T> CommonResponse<T> successWithData(T data) {
        return CommonResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(data)
                .build();
    }}
