package org.web.guffgaff.guffgaff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.web.guffgaff.guffgaff.filter.JWTFilter;
import org.web.guffgaff.guffgaff.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
       http.authorizeHttpRequests(authorizeRequests ->{
           authorizeRequests.requestMatchers("/register","/do-register", "/login", "/", "/do-register","/authenticate","/api/v1/authenticate").permitAll()
                             .requestMatchers("/css/**", "/script/**", "/images/**", "/webjars/**").permitAll()
                             .anyRequest().authenticated();

       // Allow static resources

       });
       http.sessionManagement(AbstractHttpConfigurer::disable)
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


       /* http.formLogin(login->{
           login
                   .loginPage("/login")
                   .loginProcessingUrl("/authenticate")
                   .defaultSuccessUrl("/guffgaff",true)
                   .usernameParameter("username")
                   .passwordParameter("password")
                   .failureUrl("/login?error");
        }); */

        /*
       http.logout(logout->{
           logout
                   .logoutUrl("/logout")
                   .logoutSuccessUrl("/login?logout")
                   .invalidateHttpSession(true)
                   .deleteCookies("JSESSIONID");

       }); */

        /*

       http.sessionManagement(session->{
           session
                   .invalidSessionUrl("/login?invalid")
                   .maximumSessions(1);

       }); */



        return http.build();

    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        System.out.println("Inside daoAuthenticationProvider");
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(bEncoder());
        return daoAuthenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return  authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder bEncoder() {
        return new BCryptPasswordEncoder();
    }

}
