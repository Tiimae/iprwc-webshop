package tiimae.webshop.iprwc.configurations;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tiimae.webshop.iprwc.interceptor.SecurityHeadersInterceptor;

@Configuration
@AllArgsConstructor
public class InterceptorConfigurations implements WebMvcConfigurer {

    private SecurityHeadersInterceptor securityHeadersInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityHeadersInterceptor);
    }

}
