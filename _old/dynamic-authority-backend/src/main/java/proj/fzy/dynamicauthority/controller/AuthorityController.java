package proj.fzy.dynamicauthority.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.model.dto.CommonResponse;
import proj.fzy.dynamicauthority.service.AuthorityService;

import java.util.List;

@RestController
@RequestMapping("/authority")
public class AuthorityController {
    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> create(@RequestParam String name) {
        return authorityService.createNewAuthority(name) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "duplicate authority");
    }

    @DeleteMapping("/{name}")
    public CommonResponse<Void> delete(@PathVariable String name) {
        return authorityService.deleteAuthority(name) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.NOT_FOUND.value(), "authority not exists");
    }

    @GetMapping("/{name}")
    public CommonResponse<Authority> getByName(@PathVariable String name) {
        Authority dbAuthority = authorityService.getByName(name);
        return dbAuthority == null?
                CommonResponse.<Authority>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("not found")
                        .build() :
                CommonResponse.<Authority>builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(dbAuthority)
                        .build();
    }

    @GetMapping("/all")
    public CommonResponse<List<Authority>> getAll() {
        return CommonResponse.<List<Authority>>builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(authorityService.getAll())
                        .build();
    }

}
