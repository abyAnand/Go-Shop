package io.ecommerce.GoShop.Configuration;

import io.ecommerce.GoShop.model.Role;
import io.ecommerce.GoShop.model.RoleEnum;
import io.ecommerce.GoShop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class RoleCreater {

    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    public void createRole(){
        boolean adminExists = false;
        boolean roleExists = false;
        List<Role> roleList = roleRepository.findAll();
        for(Role role : roleList){
            if(role.getRoleName().equals(RoleEnum.ROLE_ADMIN.toString())){
                adminExists = true;
            }
            if(role.getRoleName().equals(RoleEnum.ROLE_USER.toString())){
                roleExists = true;
            }
        }

        if(!adminExists){
            Role role = new Role();
            role.setRoleName(RoleEnum.ROLE_ADMIN.toString());
            roleRepository.save(role);
        }

        if(!roleExists){
            Role role = new Role();
            role.setRoleName(RoleEnum.ROLE_USER.toString());
            roleRepository.save(role);
        }

    }
}
