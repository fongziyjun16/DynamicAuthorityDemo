package stu.fzy.dynamicauthorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;
import stu.fzy.dynamicauthorization.service.VerificationService;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/verify")
    public CommonResponse<Boolean> verify(@RequestParam String username,
                                          @RequestParam String method, @RequestParam String path) {
        return verificationService.verify(username, method, path) ?
                CommonResponse.successWithData(true) :
                CommonResponse.successWithData(false);
    }
}
