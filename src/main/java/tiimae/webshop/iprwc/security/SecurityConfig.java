package tiimae.webshop.iprwc.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tiimae.webshop.iprwc.security.filters.FilterChainExceptionHandler;
import tiimae.webshop.iprwc.security.filters.JWTFilter;
import tiimae.webshop.iprwc.service.MyUserDetailsService;

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
        String [] publicUrls = new String [] {
                "/api/v1.0/to-cookie",
                "/images/**"
        };

        // Enable CORS and disable CSRF
        http.cors()
                .and()
                .userDetailsService(this.uds)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.filterChainExceptionHandler, JWTFilter.class)
                .csrf().csrfTokenRepository(this.csrfTokenRepository()).ignoringAntMatchers(publicUrls)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/api/v1.0/product/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1.0/supplier").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1.0/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1.0/brand").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1.0/csrf").permitAll()
                .antMatchers("/api/v1.0/review/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1.0/auth/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/api/v1.0/to-cookie").permitAll()
                .anyRequest().authenticated();

//        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookiePath("/");
        repository.setCookieDomain("localhost");
        return repository;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
