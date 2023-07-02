package stu.fzy.dynamicauthorization.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import stu.fzy.dynamicauthorization.service.ResourceService;
import stu.fzy.dynamicauthorization.utils.EndpointUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class InvalidEndpointCleaner {

    private final EndpointUtils endpointUtils;
    private final ResourceService resourceService;

    public InvalidEndpointCleaner(EndpointUtils endpointUtils, ResourceService resourceService) {
        this.endpointUtils = endpointUtils;
        this.resourceService = resourceService;
    }

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void invalidEndpointCleanUp() {
        Map<String, Set<String>> appEndpoints = endpointUtils.getAppEndpoints();
        Map<String, Set<String>> dbEndpoints = resourceService.getDBEndpoints();
        dbEndpoints.forEach((method, paths) -> {
            paths.forEach(path -> {
                if (!appEndpoints.containsKey(method) || !appEndpoints.get(method).contains(path)) {
                    resourceService.delete(method, path);
                }
            });
        });
    }
}
