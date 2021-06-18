package com.chat.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.version}")
    private String version;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
    http
        .addFilterBefore(
            new AuthenticationFilter(version),
            UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
            .antMatchers("/index", version + "/join**").permitAll()
            .anyRequest().authenticated()
            .and()
        .logout()
            .deleteCookies("JSESSIONID")
            .clearAuthentication(true)
        .logoutSuccessUrl("/index").permitAll();
    }

    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(sec);
        return templateEngine;
    }
}