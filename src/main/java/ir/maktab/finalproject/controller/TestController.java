package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.service.user.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class TestController {
@Autowired
private RoleServiceImpl roleService;

    @GetMapping("/ra")
    @ResponseBody
    @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    public String ra(){

        return "its worked";
    }
    @GetMapping("/info")
    @ResponseBody
    public Principal getInfo(Principal principal) {
        return principal;
    }
    @GetMapping("/test")
    public String test() {
        return "js";
    }  @GetMapping("/403")
    public String getError() {
        return "403";
    }



}
