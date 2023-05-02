package com.springboot.thymeleaf.bookstore.project.configuration;

import com.springboot.thymeleaf.bookstore.project.constant.Constants;
import com.springboot.thymeleaf.bookstore.project.security.CustomUserDetailsService;
import com.springboot.thymeleaf.bookstore.project.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment environment;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public BCryptPasswordEncoder passwordEncoder() {
        return SecurityUtils.passwordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().
                antMatchers(Constants.PUBLIC_MATCHERS)
                .permitAll().anyRequest().authenticated();
        http
                .cors().disable().csrf().disable()
                .formLogin().failureUrl("/web/bookStore/login?error")
                .loginPage("/web/bookStore/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/web/bookStore/logout")).logoutSuccessUrl("/web/bookStore/home").deleteCookies("remember-me").permitAll().and()
                .rememberMe();
    }


    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
