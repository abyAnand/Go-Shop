package io.ecommerce.GoShop.Configuration;

import io.ecommerce.GoShop.Auth.Otp.OtpService;
import io.ecommerce.GoShop.Auth.login.OTPAuthenticationFilter;
import io.ecommerce.GoShop.Auth.login.OTPAuthenticationProvider;
import io.ecommerce.GoShop.service.user.UserInfoDetailsService;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final OtpService otpService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(OtpService otpService, UserDetailsService userDetailsService, UserService userService, PasswordEncoder passwordEncoder) {
        this.otpService = otpService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsPasswordService((UserDetailsPasswordService) userService);
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/static/**").permitAll()
                        .antMatchers("/", "/user/**", "/send-otp", "/verify-otp", "/register","/verify-login-otp").permitAll()
                        .antMatchers("/index").authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/access-denied"))
                )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            for (GrantedAuthority auth : authentication.getAuthorities()) {
                                if (auth.getAuthority().equals("ROLE_ADMIN")) {
                                    response.sendRedirect("/admin/users");
                                    return;
                                }
                            }
                            response.sendRedirect("/");
                        })
                )
                .addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        OTPAuthenticationFilter filter = new OTPAuthenticationFilter(authenticationManager(), otpService);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/verify-login-otp", "POST"));
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authenticationProvider(), otpAuthenticationProvider()));
    }

    private AuthenticationProvider otpAuthenticationProvider() {
        return new OTPAuthenticationProvider(userDetailsService, userService, otpService);
    }
}
