package com.asi.hms.config;

import com.asi.hms.components.AuthTokenFilter;
import com.asi.hms.components.PasswordComponent;
import com.asi.hms.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final PasswordComponent passwordComponent;
    private final AuthTokenFilter authTokenFilter;

    private final UserService userService;

    public WebSecurityConfig(PasswordComponent passwordComponent,
                             AuthTokenFilter authTokenFilter,
                             UserService userService) {

        this.passwordComponent = passwordComponent;
        this.authTokenFilter = authTokenFilter;
        this.userService = userService;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return this.userService;
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordComponent.bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
//                .httpBasic(Customizer.withDefaults())

                .authorizeRequests()
//                    .antMatchers("/hf/**").permitAll()
                .antMatchers("/api/user/login").permitAll()
//                    .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                // Allow cors
                .and()
                .cors()

                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        ;
//
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}