package io.ecommerce.GoShop.Configuration;

import io.ecommerce.GoShop.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class UserInfoToUserDetailsConversion implements UserDetails {


    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    private final boolean isEnabled;

    public UserInfoToUserDetailsConversion(User user) {
        username = user.getUsername();
        password = user.getPassword();

        System.out.println(username );
        System.out.println(password);
        authorities = Arrays.stream(user.getRole().getRoleName().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        isEnabled = user.isEnabled();


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
