package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        User.withDefaultPasswordEncoder()
//                .username("user").password("password").roles("USER")
//                .username("admin").password("admin").roles("ADMIN")
//                .build();
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user").password("password")
                .roles("USER").and().withUser("admin").password("admin")
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/book-service/books")
                .permitAll().antMatchers("/eureka/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().formLogin().and()
                .logout().permitAll().logoutSuccessUrl("/book-service/books")
                .permitAll().and().csrf().disable();
    }
}