package ir.maktab.finalproject.controller.signup;

import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import ir.maktab.finalproject.util.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping(value = "/register")
public class SignupController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
//        model.addAttribute("error", errors);
        return "signup_form";
    }

    @PostMapping("/process")
    public String getProcessRegistration(@Valid User user, BindingResult bindingResult, HttpServletRequest request,
                                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", bindingResult.getAllErrors());
            model.addAttribute("user", new User());
            return "signup_form";
        }
        if (user.getPassword().equals(user.getRetypePassword())) {
            try {
                userService.register(user, getSiteURL(request));
                return "register_success";
            } catch (UserAlreadyExistException | UnsupportedEncodingException | MessagingException e) {
//                bindingResult.rejectValue("username", "email", "An account already exists for this email.!");
                model.addAttribute("exception",e.getMessage());
                return "signup_form";
            }


        } else {
            return "signup_form";
        }
    }

    @GetMapping("/verify")
    public String getVerifyToken(@RequestParam("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }

    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
