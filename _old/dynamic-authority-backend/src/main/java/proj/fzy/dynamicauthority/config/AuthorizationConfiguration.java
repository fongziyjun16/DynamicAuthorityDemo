package proj.fzy.dynamicauthority.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationConfiguration {
    @Getter
    @Setter
    @ToString
    public static class Resource {
        private String method;
        private String path;
    }

    private List<Resource> whiteList;

    public boolean isInWhiteList(String method, String path) {
        for (Resource resource : whiteList) {
            if (resource.getMethod().equals(method) && resource.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }
}
