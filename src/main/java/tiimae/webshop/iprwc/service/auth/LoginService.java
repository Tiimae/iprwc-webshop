package tiimae.webshop.iprwc.service.auth;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.models.Token;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.util.JWTUtil;

@Component
@AllArgsConstructor
public class LoginService {

    private JWTUtil jwtUtil;
    private UserDAO userDAO;

    public void generateTokens(User user) {
        Token accessToken = jwtUtil.generateAccessToken(user);
        user.setAccessToken(accessToken);

        Token refreshToken = jwtUtil.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);

        this.userDAO.updateByObject(user.getId(), user);
    }
}
