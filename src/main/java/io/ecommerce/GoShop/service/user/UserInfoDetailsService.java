package io.ecommerce.GoShop.service.user;

import io.ecommerce.GoShop.Configuration.UserInfoToUserDetailsConversion;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserInfoDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Getting user info from db.
        Optional<User> user =   userRepository.findByUsername(username);

        return user.map(UserInfoToUserDetailsConversion::new)
                .orElseThrow(()-> new UsernameNotFoundException("user not found:"+username));
//      }

    }


}
