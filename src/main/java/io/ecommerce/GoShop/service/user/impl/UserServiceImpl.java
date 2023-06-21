package io.ecommerce.GoShop.service.user.impl;

import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.Cart;
import io.ecommerce.GoShop.model.Role;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.RoleRepository;
import io.ecommerce.GoShop.repository.UserRepository;
import io.ecommerce.GoShop.service.user.UserService;
import io.ecommerce.GoShop.service.user.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserServiceInterface, UserDetailsPasswordService {



    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void save(User user){
        userRepository.save(user);
    }


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
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUsername(Pageable pageable, String username) {
        return userRepository.findByUsername(username, pageable);
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(long phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public String findPhoneNumberByUsername(String username) {

        Optional<User> user =  userRepository.findPhoneNumberByUsername(username);
        return String.valueOf(user.get().getPhoneNumber());
    }

    @Override
    public void saveUserWithEncodedPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(user.isEnabled());

        Role userRole = roleRepository.findByRoleName(user.getRole().getRoleName());
        user.setRole(userRole);

        userRepository.save(user);
    }

    @Override
    public void deleteCart(Cart cart) {
        User user = userRepository.findById(cart.getUser().getId()).orElse(null);
        user.setCart(null);
        this.save(user);
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
