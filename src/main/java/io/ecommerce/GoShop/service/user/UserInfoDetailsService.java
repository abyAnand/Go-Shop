package io.ecommerce.GoShop.service.user;

import io.ecommerce.GoShop.Configuration.UserInfoToUserDetailsConversion;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userInfoRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Getting user info from db.
        Optional<User> userInfo =   userInfoRepository.findByUsername(username);
        //Converting user info to UserDetails so that it can be returned.
        return userInfo.map(UserInfoToUserDetailsConversion::new)
                .orElseThrow(()-> new UsernameNotFoundException("user not found:"+username));

    }


}
