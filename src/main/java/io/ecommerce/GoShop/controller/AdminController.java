package io.ecommerce.GoShop.controller;

import io.ecommerce.GoShop.model.Role;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.role.RoleService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping("/users")
    public String userList(Model model) {


        List<User> userList = userService.findAll();
        model.addAttribute("users", userList);

        return "/admin/user-management";
    }

@GetMapping("/users/create")
public String createUser(Model model){

    List<Role> roles = roleService.getRoles();
    model.addAttribute("user", new User());
    model.addAttribute("roles", roles);


        return "/admin/create-user";
}

    @GetMapping("/users/update/{id}")
    public String updateUser(@PathVariable UUID id, RedirectAttributes attributes, Model model){

        User user = userService.findById(id);
        List<Role> role = roleService.getRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", role);


        return "/admin/update-user";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable UUID id,
                             RedirectAttributes attributes,
                             Model model) {

        Optional<User> user = Optional.ofNullable(userService.findById(id));

        if (user.isPresent()) {
            UUID userId = user.get().getId();
            userService.delete(userId);
            attributes.addFlashAttribute("message", "User deleted successfully");
        }

        return "redirect:/admin/users";
    }


    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           BindingResult result,
                           Model model) {

        List<Role> role = roleService.getRoles();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", role);
            return "/admin/new-user";
        }

        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            result.rejectValue("username", "error.username", "Username already exists");
            model.addAttribute("roles", role);
            return "/admin/new-user";
        }

        userService.save(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/saveNewUser")
    public String saveNewUser(@ModelAttribute("user") @Valid User user,
                              BindingResult result,
                              Model model) {

        List<Role> roles = roleService.getRoles();

        if (result.hasErrors()) {
            model.addAttribute("roles", roles);
            return "/admin/create-user";
        }

        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            result.rejectValue("username", "error.username", "Username already exists");
            model.addAttribute("roles", roles);
            return "/admin/create-user";
        }

        userService.save(user);

        return "redirect:/admin/users";
    }
}
