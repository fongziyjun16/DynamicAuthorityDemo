package proj.fzy.dynamicauthority.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import proj.fzy.dynamicauthority.enums.ResourcePermitAll;
import proj.fzy.dynamicauthority.exception.NotFoundException;
import proj.fzy.dynamicauthority.model.db.Resource;
import proj.fzy.dynamicauthority.repository.ResourceRepository;

import java.util.Optional;

@Component
public class InterceptorUtils {
    private final ResourceRepository resourceRepository;

    public InterceptorUtils(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public boolean isPermitAll(HttpServletRequest request) {
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(
                request.getMethod(),
                (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        if (optionalResource.isEmpty()) {
            throw new NotFoundException();
        }
        return optionalResource.get().getPermitAll() == ResourcePermitAll.FREE_TO_GO;
    }
}
