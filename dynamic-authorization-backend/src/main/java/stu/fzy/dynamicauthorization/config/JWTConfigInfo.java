package stu.fzy.dynamicauthorization.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWTConfigInfo {
    private String header;
    private String prefix;
    private String secretKey;
    private Integer duration;
}
