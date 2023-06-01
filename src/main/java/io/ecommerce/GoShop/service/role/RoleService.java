package io.ecommerce.GoShop.service.role;

import io.ecommerce.GoShop.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    List<Role> getRoles();

}
