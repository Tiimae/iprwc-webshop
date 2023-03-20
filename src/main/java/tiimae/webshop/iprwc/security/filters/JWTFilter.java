package tiimae.webshop.iprwc.security.filters;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tiimae.webshop.iprwc.exception.token.InvalidTokenException;
import tiimae.webshop.iprwc.exception.token.TokenExpiredException;
import tiimae.webshop.iprwc.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.jwtUtil.resolveAccessToken(request);
        if (token != null && !token.isBlank()) {
            // Validate token
            try {
                if (this.jwtUtil.validateAccessToken(token)) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        try {
                            Authentication authentication = this.jwtUtil.getAuthentication(token);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } catch (InvalidTokenException e) {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                        }
                    }
                }
            } catch (TokenExpiredException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
