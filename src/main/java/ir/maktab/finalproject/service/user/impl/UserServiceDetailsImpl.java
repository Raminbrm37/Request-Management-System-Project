package ir.maktab.finalproject.service.user.impl;

import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username [" + username + "] not found in the system");
        }
        if (user.getRole() == null) {
            user.setRole(roleService.setUserRole());
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(user.getRole().getAuthorities());
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

//authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
//            for (Authority role : user.getRole().getAuthorities()) {
//                authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
//            }


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView getException(UsernameNotFoundException notFoundException) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("t");
        modelAndView.addObject("message",notFoundException.getMessage());
        return modelAndView;
    }
}
