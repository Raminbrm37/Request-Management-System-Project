package ir.maktab.finalproject.config;


import ir.maktab.finalproject.config.handler.*;
import ir.maktab.finalproject.service.user.impl.UserServiceDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceDetailsImpl userServiceDetails;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/register/**","/forgot-password",
                        "/forgot-password","/reset-password","/reset-password-verify",
                        "/admin/changed-password-user","/admin/reset-password-verify")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().formLogin().loginPage("/login").successHandler(new LoginSuccessHandler()).failureForwardUrl("/fail")
                .and().rememberMe().rememberMeParameter("remember-me").rememberMeCookieName("remember")
                .and().exceptionHandling().accessDeniedPage("/403")
                .and().logout().logoutUrl("/mylogout").deleteCookies("remember")
        .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetails).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
