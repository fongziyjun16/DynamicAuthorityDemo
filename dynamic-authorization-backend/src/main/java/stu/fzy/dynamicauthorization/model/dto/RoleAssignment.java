package stu.fzy.dynamicauthorization.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleAssignment {
    private List<String> roleNames;
    // for user
    private String username;
    // for resource
    private String method;
    private String path;
    private String authType;
}
