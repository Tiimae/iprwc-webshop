package tiimae.webshop.iprwc.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import tiimae.webshop.iprwc.DAO.TokenDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.constants.TokenType;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.token.InvalidTokenException;
import tiimae.webshop.iprwc.exception.token.TokenAlreadyExistsException;
import tiimae.webshop.iprwc.exception.token.TokenExpiredException;
import tiimae.webshop.iprwc.exception.token.TokenNotFoundException;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.Token;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.MyUserDetailsService;

@Component
public class JWTUtil {
    @Autowired private MyUserDetailsService myUserDetailsService;
    @Autowired private UserDAO userDAO;
    @Autowired private TokenDAO tokenDAO;

    @Value("${issuer}")
    private String issuer;
    @Value("${jwt_secret}")
    private String secret;
    @Value("${access-token-lifetime}")
    private long accessTokenLifetime;
    @Value("${refresh-token-lifetime}")
    private long refreshTokenLifetime;

    private JWTVerifier verifier;

    public void setup() {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).withIssuer(this.issuer).build();
    }

    public Token generateAccessToken(User user) {
        try {
            if (user.getAccessToken() != null) {
                if (!user.getAccessToken().hasExpired()) {
                    return user.getAccessToken();
                }
            }

            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plusSeconds(this.accessTokenLifetime);

            String tokenValue = JWT.create()
                    .withIssuer(this.issuer)
                    .withClaim("userId", user.getId().toString())
                    .withIssuedAt(issuedAt)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(this.secret));

            Token token;

            try {
                token = new Token(tokenValue, TokenType.ACCESS_TOKEN, issuedAt, expiresAt, user);
                token = this.tokenDAO.addToken(token);
            } catch (TokenAlreadyExistsException e) {
                throw new RuntimeException(e);
            }

            return token;
        } catch (JWTCreationException e) {
            return null;
        }
    }

    public boolean validateAccessToken(String tokenValue) throws TokenExpiredException, TokenNotFoundException {
        if (verifier == null) {
            this.setup();
        }

        try {
            verifier.verify(tokenValue);
            return true;
        } catch (JWTVerificationException e) {
            Token token = this.tokenDAO.getToken(tokenValue);
            this.validateExpiration(token);

            return false;
        }
    }

    public Token generateRefreshToken(User user) {
        if (user.getRefreshToken() != null) {
            if (!user.getRefreshToken().hasExpired()) {
                return user.getRefreshToken();
            }
        }

        String value = UUID.randomUUID().toString();
        TokenType type = TokenType.REFRESH_TOKEN;

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(refreshTokenLifetime);

        // Create refresh token
        Token token;

        try {
            token = new Token(value, type, issuedAt, expiresAt, user);
            this.tokenDAO.addToken(token);
        } catch (TokenAlreadyExistsException e) {
            throw new RuntimeException(e);
        }

        return token;
    }


    public boolean validateExpiration(Token token) throws TokenExpiredException {
        if (token.hasExpired()) {
            try {
                this.tokenDAO.deleteToken(token.getId());
            } catch (TokenNotFoundException e) {
                throw new RuntimeException(e);
            }

            throw new TokenExpiredException();
        }

        return true;
    }

    public String resolveAccessToken(HttpServletRequest request) {
      String header = request.getHeader(HttpHeaders.AUTHORIZATION);

      // Get authorization header and validate
      if (header != null && header.startsWith("Bearer ")) {
          return header.split(" ")[1].trim();
      }

      return null;
  }

  public Authentication getAuthentication(String token) throws InvalidTokenException, EntryNotFoundException {
      User user = this.userDAO.getUser(getUserId(token));

      if (user.getAccessToken() != null && !user.getAccessToken().getValue().isEmpty() && !Objects.equals(user.getAccessToken().getValue(), token)) {
         throw new InvalidTokenException();
      }

      UserDetails userDetails = this.myUserDetailsService.loadUserById(getUserId(token));
      return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
  }

  
  public String getClaim(String token, String claim) {
      if (verifier == null) {
          setup();
      }

      DecodedJWT jwt = verifier.verify(token);
      return jwt.getClaim(claim).asString();
  }

  public UUID getUserId(String token) {
      String userId = getClaim(token, "userId");
      return UUID.fromString(userId);
  }
}
