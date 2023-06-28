package proj.fzy.dynamicauthority.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proj.fzy.dynamicauthority.service.VerificationService;

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }


}
