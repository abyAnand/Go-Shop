package io.ecommerce.GoShop.service.user;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public void save(UserDTO userdto);


    Optional<User> findByUsername(String username);

    List<User> findAll();
}
