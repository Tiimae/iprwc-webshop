package tiimae.webshop.iprwc.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfigurations implements WebMvcConfigurer {
   

   @Override
   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200", "http://localhost:8080")
//          .allowedOrigins("https://www.timdekok.nl", "https://timdekok.nl", "https://api.timdekok.nl:8080")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("Authorization", "X-XSRF-TOKEN", "Strict-Transport-Security", "X-Frame-Options", "X-Content-Type-Options", "content-type", "content-security-policy")
            .allowCredentials(true);
   }
}
