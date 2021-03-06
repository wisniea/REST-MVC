package pl.wisniea.restmvc_web.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.wisniea.restmvc_web.filters.JwtRequestFilter;
import pl.wisniea.restmvc_data.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN = "/login";

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserService userService, JwtRequestFilter jwtRequestFilter) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                     .loginPage(LOGIN).permitAll()
                     .loginProcessingUrl(LOGIN)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests()
                    .antMatchers("/h-2console/**").hasAnyRole("ADMIN")
                    .antMatchers(LOGIN).permitAll()
                    .antMatchers("/activation").permitAll()
                    .antMatchers(HttpMethod.POST,"/rest/login").permitAll()
                    .antMatchers("/rest/books/**").permitAll()
                    .antMatchers("/rest/cart/**").permitAll()
                    .antMatchers("/rest/**").hasRole("ADMIN")
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                    .antMatchers(HttpMethod.POST,"/users").permitAll()
                .anyRequest()
                .authenticated();

        http.headers().frameOptions().disable();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    ServletRegistrationBean<WebServlet> h2servletRegistration() {
        ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<>(new WebServlet());
        registrationBean.addUrlMappings("/h2-console/*");
        return registrationBean;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
