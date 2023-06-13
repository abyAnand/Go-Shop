package io.ecommerce.GoShop.service.user;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {
    void save(UserDTO userdto);
    Optional<User> findByUsername(String username);
    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    Page<User> findByUsername(Pageable pageable, String username);
    User findById(UUID uuid);
    void delete(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(long phoneNumber);
    void saveUserWithEncodedPassword(User user);
}