package ir.maktab.finalproject.config.handler;

import ir.maktab.finalproject.model.entity.Authority;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.repository.UserRepository;
import ir.maktab.finalproject.service.user.impl.RoleServiceImpl;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private UserRepository userRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public User loadUser(String usernmae) {
        return userService.findByUserName(usernmae);
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println(authentication.getDetails());
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        SimpleGrantedAuthority crm = new SimpleGrantedAuthority("CRM");
        SimpleGrantedAuthority user = new SimpleGrantedAuthority("USER");
        System.out.println(authentication.getAuthorities());
        if (authentication.getAuthorities().contains(admin)) {

            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/admin/advanced-search");

        }else if(authentication.getAuthorities().contains(crm)){
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/crm");
        }else if(authentication.getAuthorities().contains(user)){
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/customer");
        }

//        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/test");
    }
}
