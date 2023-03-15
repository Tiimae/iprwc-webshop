package tiimae.webshop.iprwc.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityHeadersInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.addHeader("Content-Security-Policy", "default-src 'self'");
        response.addHeader("Referrer-Policy", "same-origin");
        response.addHeader("Permissions-Policy", "geolocation=(self 'http://localhost'), microphone=()");

        return true;
    }
}
