package io.ecommerce.GoShop.controller.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class dashboard {

    @GetMapping
    public String getDashboard(){
        return "app-admin/dashboard";
    }
}
