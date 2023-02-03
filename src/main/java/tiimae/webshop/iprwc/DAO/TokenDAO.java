package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.TokenRepository;
import tiimae.webshop.iprwc.exception.token.TokenAlreadyExistsException;
import tiimae.webshop.iprwc.exception.token.TokenNotFoundException;
import tiimae.webshop.iprwc.models.Token;

@AllArgsConstructor
@Component
public class TokenDAO {

    private TokenRepository tokenRepository;

    public Token addToken(Token token) throws TokenAlreadyExistsException, DataIntegrityViolationException {
        if (this.tokenRepository.existsByValue(token.getValue())) {
            throw new TokenAlreadyExistsException();
        }

        return this.tokenRepository.save(token);
    }

    public Token getToken(UUID id) throws TokenNotFoundException {
        Optional<Token> token = this.tokenRepository.findById(id);
        if (token.isEmpty()) {
            throw new TokenNotFoundException();
        }
        return token.get();
    }


    public Token getToken(String value) throws TokenNotFoundException {
        Optional<Token> token = this.tokenRepository.findByValue(value);
        if (token.isEmpty()) {
            throw new TokenNotFoundException();
        }
        return token.get();
    }

    @Transactional
    public void deleteToken(UUID id) throws TokenNotFoundException {
        if (!this.tokenRepository.existsById(id)) {
            throw new TokenNotFoundException();
        }

        final Token token = this.getToken(id);

        token.getUser().setAccessToken(null);
        token.getUser().setRefreshToken(null);

        this.tokenRepository.delete(token);
    }

    @Transactional
    public void deleteTokensByUserId(UUID id) {
        if (!this.tokenRepository.existsByUserId(id)) {
            return;
        }

        final List<Token> allByUserId = this.tokenRepository.findAllByUserId(id);

        if (allByUserId.size() != 0) {
            for (Token token : allByUserId) {
                token.getUser().setRefreshToken(null);
                token.getUser().setAccessToken(null);
                token.setUser(null);
                this.tokenRepository.deleteById(token.getId());
            }
        }

        return;
    }
}
