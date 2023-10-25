package br.com.usp.mac0332.snippettool.config;

import br.com.usp.mac0332.snippettool.service.UserService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        // We are disabling CSRF so that our forms don't complain about a CSRF token.
        // Beware that it can create a security vulnerability
        return http.csrf(AbstractHttpConfigurer::disable)
                // Here we are configuring our login form
//                .formLogin(formLogin -> {
//                            formLogin
//                                    .loginPage("/login") // Login page will be accessed through this endpoint. We will create a controller method for this.
//                                    .loginProcessingUrl("/login-processing") // This endpoint will be mapped internally. This URL will be our Login form post action.
//                                    .usernameParameter("username")
//                                    .passwordParameter("password")
//                                    .permitAll() // We re permitting all for login page
//                                    .defaultSuccessUrl("/") // If the login is successful, user will be redirected to this URL.
//                                    .failureUrl("/login?error=true"); // If the user fails to login, application will redirect the user to this endpoint
//                        }
//                )
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers ("/login-test").permitAll()
                            .anyRequest().rememberMe();
                })
                .rememberMe(httpSecurityRememberMeConfigurer ->
                    httpSecurityRememberMeConfigurer
                            .alwaysRemember(true)
                            .tokenValiditySeconds(60)
                            .rememberMeCookieName("teste")
                            .key("segredo")
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                )
                .addFilterBefore((Filter) new CheckAuthCookieFilter(), BasicAuthenticationFilter.class)
                .build();
    }

}

