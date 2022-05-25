package com.trebnikau.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

import javax.sql.DataSource;

@EnableWebSecurity // Аннотация уже является конфигурацией
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//        auth.inMemoryAuthentication().
//                withUser(userBuilder.
//                        username("anton").
//                        password("anton").
//                        roles("EMPLOYEE")).
//                withUser(userBuilder.
//                        username("elena").
//                        password("elena").
//                        roles("HR")).
//                withUser(userBuilder.
//                        username("ivan").
//                        password("ivan").
//                        roles("MANAGER", "HR"));
        // Указываем конфигурации, что информация о user-ах находится в бд
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    // Метод для разрешение доступа к URL для конкретных ролей
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers("/").hasAnyRole(
                        "EMPLOYEE",
                        "HR",
                        "MANAGER").
                antMatchers("/hr_info").hasRole("HR").
                antMatchers("/manager_info").hasRole("MANAGER").
                // Форма логина и пароля будет запрашиваться у всех
                        and().formLogin().permitAll();
    }
}
