package com.matiasae.springmvcsecurityjdbc.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class DemoSecurityConfig {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT user_id, pw, active FROM members WHERE user_id = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id, role FROM roles WHERE user_id = ?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers("/").hasRole("EMPLOYEE")
                .requestMatchers("/leaders/**").hasRole("MANAGER")
                .requestMatchers("/admins/**").hasRole("ADMIN")
                .requestMatchers("/access-denied").permitAll()

                .requestMatchers("/").hasRole("EMPLOYEE")
                .requestMatchers("/leaders/**").hasRole("MANAGER")
                .requestMatchers("/admins/**").hasRole("ADMIN")
        )
        .formLogin(form -> form
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()

        ).exceptionHandling(configurer -> configurer
                .accessDeniedPage("/access-denied")

        ).logout(logout -> logout.permitAll());

        return http.build();
    }
}

//     @Bean
//     public InMemoryUserDetailsManager userDetailsManager() {
//         UserDetails john = User.builder()
//                 .username("john")
//                 .password("{noop}test123")
//                 .roles("EMPLOYEE")
//                 .build();
        
//         UserDetails mary = User.builder()
//                 .username("mary")
//                 .password("{noop}test123")
//                 .roles("EMPLOYEE", "MANAGER")
//                 .build();

//         UserDetails susan = User.builder()
//                 .username("susan")
//                 .password("{noop}test123")
//                 .roles("EMPLOYEE", "MANAGER", "ADMIN")
//                 .build();

//         return new InMemoryUserDetailsManager(john, mary, susan);
//     }

