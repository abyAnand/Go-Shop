package io.ecommerce.GoShop.service.user.impl;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.Role;
import io.ecommerce.GoShop.model.RoleEnum;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.RoleRepository;
import io.ecommerce.GoShop.repository.UserRepository;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    public void save(UserDTO userdto){
        User user = new User();


        user.setFirstName(userdto.getFirstName());
        user.setLastName(userdto.getLastName());
        user.setEmail(userdto.getEmail());
        user.setPhoneNumber(userdto.getPhoneNumber());
        user.setUsername(userdto.getUsername());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        user.setEnabled(true);

        Role role = roleRepository.findByRoleName("ROLE_USER");

        user.setRole(role);


        userRepository.save(user);




    }

    @Override
    public void save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(user.isEnabled());

        Role userRole = roleRepository.findByRoleName(user.getRole().getRoleName());
        user.setRole(userRole);

        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
