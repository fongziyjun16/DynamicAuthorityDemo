package proj.fzy.dynamicauthority.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserInfo {
    private String username;
    private List<String> roleNames;
    private List<String> authorityNames;
}
