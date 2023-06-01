package io.ecommerce.GoShop.service.role;

import io.ecommerce.GoShop.model.Role;
import io.ecommerce.GoShop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
     RoleRepository roleRepository;


    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
