package tiimae.webshop.iprwc.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.security.filter.JWTFilter;
import tiimae.webshop.iprwc.service.MyUserDetailService;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepo;
    @Autowired private JWTFilter filter;
    @Autowired private MyUserDetailService uds;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/api/v1.0/product/**").permitAll()
                // .antMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE}), "/api/v1.0/product/**").hasRole("Admin")
                // .antMatchers(HttpMethod.GET, "/api/v1.0/supplier/**").permitAll()
                // .antMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE}), "/api/v1.0/supplier/**").hasRole("Admin")
                // .antMatchers(HttpMethod.GET, "/api/v1.0/category/**").permitAll()
                // .antMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE}), "/api/v1.0/category/**").hasRole("Admin")
                // .antMatchers(HttpMethod.GET, "/api/v1.0/brand/**").permitAll()
                // .antMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE}), "/api/v1.0/brand/**").hasRole("Admin")
                // .antMatchers(HttpMethod.GET, "/api/v1.0/user/roles").hasRole("User")
                // .antMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.PUT, HttpMethod.GET}), "/api/v1.0/user/**").hasRole("User")
                // .antMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.DELETE}), "/api/v1.0/user/**").hasRole("Admin")
                // .antMatchers("/api/v1.0/auth/**").permitAll()
                // .antMatchers("/api/v1.0/order/**").hasRole("User")
                // .antMatchers("/api/v1.0/role/**").hasRole("Admin")
                .and()
                .userDetailsService(uds)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .enableSessionUrlRewriting(true);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
