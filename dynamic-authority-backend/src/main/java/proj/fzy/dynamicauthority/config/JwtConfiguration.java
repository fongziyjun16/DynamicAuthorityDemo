package proj.fzy.dynamicauthority.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@ToString
public class JwtConfiguration {
    private String header;
    private String prefix;
    private String secretKey;
    private Integer duration;
}
