package org.sid.jeehospitalmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder passwordEncoder = passwordEncoder();

        System.out.println("********************************");
        System.out.println(passwordEncoder.encode("1234"));
        System.out.println("********************************");

        auth.inMemoryAuthentication()
               .withUser("leon")
               .password(passwordEncoder.encode("1234"))
               .roles("USER");

        auth.inMemoryAuthentication()
                .withUser("nick")
                .password(passwordEncoder.encode("1234"))
                .roles("USER");

        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN","USER");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
        //http.httpBasic();
        http.authorizeRequests()
                .antMatchers("/save**/**","/delete**/**", "/form**/**")
                .hasRole("ADMIN");

        http.exceptionHandling().accessDeniedPage("/notAuthorized");

        http.authorizeRequests().anyRequest().authenticated();

        /* What is CSRF ?
         ** Cross-Site Request Forgery (CSRF) is an attack that forces
         ** an end user to execute unwanted actions on a web application
         ** in which they're currently authenticated.
         */

        //To activate the Cross-Site Request Forgery mechanism
       // http.csrf();

        //To desable the Cross-Site Request Forgery mechanism
       // http.csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
