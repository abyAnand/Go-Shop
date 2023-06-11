package io.ecommerce.GoShop.service.user;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    public void save(UserDTO userdto);

    public void save(User user);


    Optional<User> findByUsername(String username);

    List<User> findAll();

    User findById(UUID uuid);

    void delete(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(long phoneNumber);


    String findPhonenUmberByUsername(String username);

}
