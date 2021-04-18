package ir.maktab.finalproject.controller.admin;

import ir.maktab.finalproject.model.dto.UserSearch;
import ir.maktab.finalproject.model.entity.Role;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.service.user.impl.RoleServiceImpl;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import ir.maktab.finalproject.util.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
//@SessionAttributes(value = "operator")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('OP_INDEX_ADMIN')")
    @GetMapping
    public String getMainPage() {
        return "login-handler/admin/admin-index";
    }

    public User getDetectMe(String username) {
        return userService.findByUserName(username);

    }

    @PreAuthorize("hasAuthority('OP_ALL_USER_ADMIN')")
    @GetMapping("/all-user")
    public String getAllOfUser(Model model, Principal principal, HttpServletRequest request) {

        User currentUser = getDetectMe(((principal)).getName());
        List<User> userSet = userService.findUserByRoleNotNull();
        request.getSession(false).setAttribute("me", currentUser);
        model.addAttribute("users", userSet);
        return "admin/all-user";
    }

    @PreAuthorize("hasAuthority('OP_SHOW_USER_ADMIN')")
    @GetMapping("/show-user")
    public String getUpdatePage(@RequestParam Long id, Model model,
                                @ModelAttribute("exception") String exception,HttpServletRequest request) {
        System.out.println(exception);
        User byId = userService.findById(id);
        model.addAttribute("user",byId );
        request.getSession().setAttribute("currentUser",byId);
//        model.addAttribute("currentUser", byId);
        model.addAttribute("exception", exception);
        return "admin/user/show-information-user";
    }

    @PreAuthorize("hasAuthority('OP_EDIT_USER_ADMIN')")
    @PostMapping("/update-user")
    public String getUpdateUser(@ModelAttribute User user, Model model,@SessionAttribute("currentUser") User currentUser
            ,RedirectAttributes attributes) throws UserAlreadyExistException {
     try {
         userService.updateUserByFullInfo(user,currentUser);
         return "redirect:/admin/show-user?id="+currentUser.getId();
     }catch (UserAlreadyExistException e){
         //TODO: clear session
         attributes.addFlashAttribute("exception",e.getMessage());
         return "redirect:/admin/show-user?id="+currentUser.getId();
     }


    }

    @PreAuthorize("hasAuthority('OP_EDIT_USER_ADMIN')")
    @GetMapping("/change-status-user")
    public String getChangeUserStatus(@RequestParam String username) {

        userService.changeUserStatus(username);
        return "redirect:/admin/all-user";
    }

    @PreAuthorize("hasAuthority('OP_EDIT_USER_ADMIN')")
    @GetMapping("/change-role-user")
    public String getChangeRoleUser(@RequestParam String username, Model model) {
        User user = userService.findByUserName(username);
//        user.setRole(roleService.findById(2L));
//        userService.save(user);

        List<Role> roles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin/user/changing-role-user";
    }

    @PreAuthorize("hasAuthority('OP_EDIT_USER_ADMIN')")
    @PostMapping("/changed-role-user")
    public String changedRoleUser(User user, Long id) {
        Role role = roleService.findById(user.getRole().getId());
        User byId = userService.findById(id);
        byId.setRole(role);
        userService.save(byId);
        userService.save(user);
        return "redirect:/admin/all-user";
    }

    @PreAuthorize("hasAuthority('OP_EDIT_PASSWORD_ADMIN')")
    @GetMapping("/change-password-user")
    public String getChangePasswordUser(String username, Model model) {
        model.addAttribute("user", userService.findByUserName(username));

        return "admin/user/change-password-user";
    }

    @PreAuthorize("hasAuthority('OP_EDIT_PASSWORD_ADMIN')")
    @PostMapping("/changed-password-user")
    public String getChangedPassword(Long id, String password, String retypePassword, Model model) {
        User user = userService.findById(id);
        if (password.equals(retypePassword)) {
            userService.changePasswordUser(user, password);
            return "admin/user/successfully-change-password";
        } else {
            model.addAttribute("user", user);
            return "admin/user/change-password-user";
        }

    }

    @PreAuthorize("hasAuthority('OP_SEARCH_ADMIN')")
    @GetMapping("/advanced-search")
    public String getAdvancedSearch(Model model, RedirectAttributes redirectAttributes, Principal principal, HttpServletRequest request
            , @ModelAttribute("user-result") ArrayList<User> users) {
        User currentUser = getDetectMe(((principal)).getName());
        request.getSession().setAttribute("me", currentUser);
        model.addAttribute("users", users);
        model.addAttribute("test", new UserSearch());
        return "admin/user/advanced-search-user";
    }

    @PreAuthorize("hasAuthority('OP_SEARCH_ADMIN')")
    @PostMapping("/advanced-search")
    public String getPostAdvancedSearch(UserSearch userSearch, Model model, RedirectAttributes redirectAttributes) {

        List<User> userSet = userService.getAdvancedSearch(userSearch);

        redirectAttributes.addFlashAttribute("user-result", userSet);
        return "redirect:/admin/advanced-search";
    }

    @PreAuthorize("hasAuthority('OP_CREATE_USER_ADMIN')")
    @GetMapping("/create-user")
    public String getCreateUserPage(Model model, @ModelAttribute("user") User user
    ) {
        model.addAttribute("user", user);
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        return "admin/create-user";
    }

    @PreAuthorize("hasAuthority('OP_CREATE_USER_ADMIN')")
    @PostMapping("/create-user")
    public String getCreteUser(@Valid @ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               Model model) throws UnsupportedEncodingException, MessagingException, UserAlreadyExistException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", bindingResult.getAllErrors());
            model.addAttribute("user", user);
            List<Role> roles = roleService.findAll();
            model.addAttribute("roles", roles);
            return "admin/create-user";
        }
        if (user.getPassword().equals(user.getRetypePassword())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            try {
                userService.saveUserByAdmin(user);
                return "redirect:/admin/advanced-search";
            } catch (UserAlreadyExistException e) {
                model.addAttribute("exception", e.getMessage());
                return "admin/create-user";
            }

        } else {
            return "admin/create-user";
        }
    }
}
