package io.ecommerce.GoShop.Configuration;

import io.ecommerce.GoShop.repository.RoleRepository;
import io.ecommerce.GoShop.repository.UserRepository;
import io.ecommerce.GoShop.service.user.UserService;
import io.ecommerce.GoShop.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServiceConfig(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository, passwordEncoder, roleRepository);
    }

}