package proj.fzy.dynamicauthority.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CommonResponse <T> {
    private Integer code;
    private String message;
    private T data;

    public static CommonResponse<Void> simpleResponse(Integer code, String message) {
        return CommonResponse.<Void>builder()
                .code(code)
                .message(message)
                .build();
    }
}
