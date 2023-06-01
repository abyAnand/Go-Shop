package io.ecommerce.GoShop.Configuration;


import io.ecommerce.GoShop.service.user.UserInfoDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/static/**")
                .permitAll()
                .antMatchers("/index")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
                .successHandler(new RefererAuthenticationSuccessHandler())
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");// Custom forbidden error page
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf().disable()
//                .authorizeHttpRequests()
//                .antMatchers("/static/**")
//                .permitAll()
//                .and()
//                .authorizeHttpRequests()
//                .antMatchers("/index")
//                .authenticated().and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .defaultSuccessUrl("/", true)
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/403") // Custom forbidden error page
//                .and()
//                .formLogin()
//                .successHandler((request, response, authentication) -> {
//                    for (GrantedAuthority auth : authentication.getAuthorities()) {
//                        if (auth.getAuthority().equals("ROLE_ADMIN")) {
//                            response.sendRedirect("/dashboard/admin");
//                        }
//                    }
//                })
//                .and()
//                .build();
//    }


    //password encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        //giving info about who is the user details service and password encoder
        //these info can be used to generate user details object and set it to authentication object.
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserInfoDetailsService();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring().antMatchers("/static/**","/templates/**");
    }

//    public class RefererRedirectionAuthenticationSuccessHandler
//            extends SimpleUrlAuthenticationSuccessHandler
//            implements AuthenticationSuccessHandler {
//
//        public RefererRedirectionAuthenticationSuccessHandler() {
//            super();
//            setUseReferer(true);
//        }
//
//    }
}