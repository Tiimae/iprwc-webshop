package tiimae.webshop.iprwc.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.security.filters.FilterChainExceptionHandler;
import tiimae.webshop.iprwc.security.filters.JWTFilter;
import tiimae.webshop.iprwc.service.MyUserDetailsService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private JWTFilter jwtFilter;
    private FilterChainExceptionHandler filterChainExceptionHandler;
    private MyUserDetailsService uds;

    /**
     * Configure Spring's Security Filter Chain for HTTP Security.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .userDetailsService(this.uds)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and();

        // Set permissions on endpoints
        http.authorizeHttpRequests()
                // Public endpoints
                .antMatchers(HttpMethod.GET, "/api/v1.0/product/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1.0/supplier/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1.0/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1.0/brand/**").permitAll()
                .antMatchers("/api/v1.0/auth/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/api/v1.0/to-cookie").permitAll()

                // Private endpoints
                .anyRequest().authenticated();


        // Add JWT token filter
        http.addFilterBefore(
                this.jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterBefore(
                this.filterChainExceptionHandler,
                JWTFilter.class
        );

//        return http.build();
    }

    /**
     * Initialize a password encoder to use for encoding a user's password.
     * By using an instance of <code>BCryptPasswordEncoder</code> provided by Spring Security, passwords are
     * automatically hashed and a salt is added to the password.
     *
     * @return an instance of the BCryptPasswordEncoder class which implements the BCrypt strong hashing function.
     * @see BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Initialize an authentication manager which will be used for authenticating users.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
