package org.springframework.samples.petclinic.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("production")
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("bill").password("123456").roles("USER");
        auth.inMemoryAuthentication().withUser("stephen").password("1234567").roles("USER", "EDIT");
        auth.inMemoryAuthentication().withUser("samantha").password("12345678").roles("USER", "ADD", "EDIT");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**/new").access("hasRole('ROLE_ADD')")
            .antMatchers("/**").access("hasRole('ROLE_ADD') or hasRole('ROLE_USER') or hasRole('ROLE_EDIT')")
            .antMatchers("/**/edit").access("hasRole('ROLE_EDIT')")
            .and().formLogin();

    }
}
