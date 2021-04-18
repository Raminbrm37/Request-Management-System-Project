package ir.maktab.finalproject.controller.login;

import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import ir.maktab.finalproject.util.exception.UserAlreadyExistException;
import ir.maktab.finalproject.util.token.CustomToken;
import ir.maktab.finalproject.util.token.CustomTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Controller
public class LoginController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CustomTokenServiceImpl tokenService;

    @GetMapping("/")
    public String getRedirectPage(Authentication authentication){
        if (authentication==null){
            return "index";
        }
        if (authentication.isAuthenticated()){
            SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
            SimpleGrantedAuthority crm = new SimpleGrantedAuthority("CRM");
            SimpleGrantedAuthority user = new SimpleGrantedAuthority("USER");
            if (authentication.getAuthorities().contains(admin)) {
                return "redirect:/admin/advanced-search";
            }else if(authentication.getAuthorities().contains(crm)){
                return "redirect:/crm";
            }else if(authentication.getAuthorities().contains(user)){
                return "redirect:/customer";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model,Authentication authentication,@ModelAttribute("message") String message) throws UsernameNotFoundException{
        if (authentication==null){
            model.addAttribute("message",message);
            return "login";
        }
        if (authentication.isAuthenticated()){
            SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
            SimpleGrantedAuthority crm = new SimpleGrantedAuthority("CRM");
            SimpleGrantedAuthority user = new SimpleGrantedAuthority("USER");
            if (authentication.getAuthorities().contains(admin)) {
                return "redirect:/admin/advanced-search";
            }else if(authentication.getAuthorities().contains(crm)){
                return "redirect:/crm";
            }else if(authentication.getAuthorities().contains(user)){
                return "redirect:/customer";
            }
        }

        return "login";
    }

    @GetMapping("/forgot-password")
    public String getForgotPassword() {
        return "login/forgot-password-page";
    }

    @PostMapping("/reset-password")
    public String getResetPage(String email) throws UnsupportedEncodingException, MessagingException {
        User user = userService.findByEmail(email);
        if (user != null) {
            userService.sendVerificationTokenByEmail(user);
            return "login/email-send-reset-password";
        } else {
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/reset-password-verify")
    public String getTokenFromVerificationEmail(String token, Model model) {
        CustomToken customToken = tokenService.findByName(token);
        User user = userService.findById(customToken.getUser().getId());
        if (customToken == null || user == null || customToken.isExpired()) {
            return "403";
        } else {
            tokenService.remove(customToken);
            model.addAttribute("user", user);
            return "admin/user/change-password-user";
        }
    }


    @GetMapping("/user/my-profile/{username}")
    public String getUpdatePage(@PathVariable("username") String username, Model model
            , @ModelAttribute("exception") String exception, HttpServletRequest request) {
        User byId = userService.findCurrentUser(username);
        request.getSession().setAttribute("currentUser",byId);
        model.addAttribute("user",byId );
        model.addAttribute("exception",exception );
        return "login/user-myprofile";
    }

    @PostMapping("/user/edit-profile")
    public String getUpdateUser(@ModelAttribute User user, Model model, RedirectAttributes attributes,
                                @SessionAttribute("currentUser") User currentUser, SessionStatus sessionStatus) throws UserAlreadyExistException {
        try {
            userService.updateUserByFullInfo(user,currentUser);
            return "redirect:/user/my-profile/" + currentUser.getUsername();

        }catch (UserAlreadyExistException e){
            //TODO: clear session
            attributes.addFlashAttribute("exception",e.getMessage());
            return "redirect:/user/my-profile/"+currentUser.getUsername();
        }

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView getException(UsernameNotFoundException notFoundException) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("t");
        modelAndView.addObject("message",notFoundException.getMessage());
        return modelAndView;
    }

    @PostMapping("/fail")
    public String getFailureLogin(RedirectAttributes redirectAttributes,String username){
        if (userService.findByUserName(username)==null){
            redirectAttributes.addFlashAttribute("message",username);
        }else {
            redirectAttributes.addFlashAttribute("message2",username);
        }

        return "redirect:/login";
    }

}
