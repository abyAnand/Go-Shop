package io.ecommerce.GoShop.controller;


import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.Address;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.address.AddressService;
import io.ecommerce.GoShop.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AddressService addressService;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String dashboard(Model model){

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);
        model.addAttribute("user",user);

        return "user/user-dashboard";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           BindingResult result,
                           Model model) {


        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/admin/update-user";
        }

        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            result.rejectValue("username", "error.username", "Username already exists");
            return "/user/user-dashboard";
        }
        User thisUser = userService.findById(user.getId());

        thisUser.setFirstName(user.getFirstName());
        thisUser.setLastName(user.getLastName());
        thisUser.setEmail(user.getEmail());
        thisUser.setPhoneNumber(user.getPhoneNumber());
        thisUser.setUsername(user.getUsername());

        if(user.getPassword() != null){
            thisUser.setPassword(user.getPassword());
            userService.saveUserWithEncodedPassword(thisUser);
            return "redirect:/";
        }else{
            userService.save(thisUser);
        }


        return "redirect:/";
    }


    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable UUID id, Model model){

        User user = userService.findById(id);

        return "index";
    }

    @GetMapping("/category")
    public String shop(){
        return "category";
    }

    @GetMapping("/address")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String addressDashboard(Model model){

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);

        List<Address> userAddress =  user.getAddresses().stream().filter(address -> !address.isDeleted())
                        .toList();


        model.addAttribute("user", user);
        model.addAttribute("address",userAddress);
        return "user/address-dashboard";
    }


    @GetMapping("/address/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String addAddress(Model model){
        return "user/address-form";
    }

    @PostMapping("/address/save")
    public String saveAddress(Model model,
                              @ModelAttribute Address address){

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);

        address.setUser(user);

        addressService.save(address);
        model.addAttribute("user",user);

        return "user/user-dashboard";
    }

    @PostMapping("/address/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String updateAddress(@ModelAttribute Address address,
                                Model model){


        return "redirect:/user/address";
    }


    @GetMapping("/address/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String updateAddress(@PathVariable UUID id,
                                Model model){

        Address address = addressService.findById(id).orElse(null);

        model.addAttribute("address",address);

        return "user/update-address";

    }

    @GetMapping("/address/delete/{id}")
    public String deleteAddress(@PathVariable UUID id,
                                Model model){

        Address address = addressService.findById(id).orElse(null);

        addressService.deleteAddress(address);


        return "redirect:/user/address";

    }



}
