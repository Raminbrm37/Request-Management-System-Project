package ir.maktab.finalproject.controller.admin.role;

import ir.maktab.finalproject.model.entity.Role;
import ir.maktab.finalproject.service.user.impl.RoleServiceImpl;
import ir.maktab.finalproject.util.exception.RoleAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/role")
@PreAuthorize("hasAuthority('OP_ROLE_MANAGEMENT_ADMIN')")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public String getCreatePage(Model model, @ModelAttribute("exception") String exception) {
        model.addAttribute("role", new Role());
        model.addAttribute("exception", exception);
        return "admin/create-role";
    }

    @PostMapping("/created-role")
    public String getCreateRole(@ModelAttribute Role role, Model model, RedirectAttributes redirectAttributes) throws RoleAlreadyExistException {
        try {
            roleService.saveByException(role);
            model.addAttribute("role", role);
            return "admin/save-role";
        }catch (RoleAlreadyExistException e){
            redirectAttributes.addFlashAttribute("exception",e.getMessage());
            return "redirect:/role";
        }

    }

    @GetMapping("/all")
    public String getAllRole(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/all-roles";
    }

    @GetMapping("/edit-role")
    public String getEditRole(Model model, @RequestParam String name) {
        model.addAttribute("role", roleService.findByName(name));
        return "admin/role/view-role";
    }

    @PostMapping("/update-role")
    public String getUpdateRole(Role role) {

        roleService.save(role);

        return "redirect:/role/all";
    }

    @GetMapping("/delete-role")
    public String getDeleteRole(Model model, @RequestParam String name) {
        Role role = roleService.findByName(name);
        roleService.remove(role);
        return "redirect:/role/all";
    }

}
