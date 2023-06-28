package proj.fzy.dynamicauthority.untils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class EndpointUtils {
    private final ApplicationContext applicationContext;

    public EndpointUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Map<String, Set<String>> getEndpointInfo() {
        Map<String, Set<String>> endpoints = new HashMap<>();
        Set<RequestMappingInfo> requestMappingInfos = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class)
                .getHandlerMethods()
                .keySet();
        for (RequestMappingInfo requestMappingInfo : requestMappingInfos) {
            if (!requestMappingInfo.getMethodsCondition().isEmpty()) {
                Set<String> patterns = null;
                Set<String> pathPatterns = null;
                // handle patterns
                if (requestMappingInfo.getPatternsCondition() != null &&
                        !requestMappingInfo.getPatternsCondition().isEmpty()) {
                    patterns = requestMappingInfo.getPatternsCondition().getPatterns();
                }
                // handle path patterns
                if (requestMappingInfo.getPathPatternsCondition() != null &&
                        !requestMappingInfo.getPathPatternsCondition().isEmpty()) {
                    pathPatterns = requestMappingInfo.getPathPatternsCondition().getPatternValues();
                }
                // handle methods
                Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
                for (RequestMethod method : methods) {
                    String httpMethod = method.name();
                    if (!endpoints.containsKey(httpMethod)) {
                        endpoints.put(httpMethod, new HashSet<>());
                    }
                    Set<String> paths = endpoints.get(httpMethod);
                    if (patterns != null) {
                        paths.addAll(patterns);
                    }
                    if (pathPatterns != null) {
                        paths.addAll(pathPatterns);
                    }
                }
            }
        }
        return endpoints;
    }

}
