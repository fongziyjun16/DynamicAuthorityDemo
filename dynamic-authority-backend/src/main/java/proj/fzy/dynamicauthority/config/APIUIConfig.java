package proj.fzy.dynamicauthority.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIUIConfig {
    @Bean
    public OpenAPI general() {
        return new OpenAPI()
                .info(new Info()
                                .title("Dynamic Authority")
                                .description("Dynamic Authority Demo")
                                .version("0.1"));
    }
}
