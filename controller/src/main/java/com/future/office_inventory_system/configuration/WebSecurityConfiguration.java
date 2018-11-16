package com.future.office_inventory_system.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.service.UserAuthService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthService).passwordEncoder(User.PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint((httpServletRequest, httpServletResponse, e) ->
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/api/login")
                    .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                        httpServletResponse.setContentType("application/json;charset=UTF-8");
                        ObjectMapper mapper = new ObjectMapper();
                        String json = mapper.writeValueAsString(loggedinUserInfo.getUser());
                        httpServletResponse.getWriter().print(json);
                    })
                    .failureHandler((httpServletRequest, httpServletResponse, e) -> httpServletResponse
                            .setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                    .permitAll()
                .and()
                .logout().logoutUrl("/api/logout")
                    .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) ->
                            httpServletResponse.setStatus(HttpServletResponse.SC_OK))
                    .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}