package proj.fzy.dynamicauthority.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import proj.fzy.dynamicauthority.model.dto.CommonResponse;
import proj.fzy.dynamicauthority.service.VerificationService;

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping(value = "/verify")
    public CommonResponse<Void> verify(
            @RequestParam String username, @RequestParam String method, @RequestParam String path) {
        return verificationService.verify(username, method, path) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.FORBIDDEN.value(), "insufficient authorities");
    }
}
