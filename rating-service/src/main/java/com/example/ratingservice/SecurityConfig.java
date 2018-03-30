package com.example.ratingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .regexMatchers("^/ratings\\?bookId.*$").authenticated()
                .antMatchers(HttpMethod.POST,"/ratings").authenticated()
                .antMatchers(HttpMethod.PATCH,"/ratings/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/ratings/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/ratings").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/hystrix").authenticated()
                .anyRequest().authenticated()
                .and()
                .httpBasic().and()
                .csrf()
                .disable();
    }
}

