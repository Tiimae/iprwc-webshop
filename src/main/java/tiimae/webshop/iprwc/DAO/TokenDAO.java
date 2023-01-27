package tiimae.webshop.iprwc.DAO;

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


    public void deleteToken(UUID id) throws TokenNotFoundException {
        if (!this.tokenRepository.existsById(id)) {
            throw new TokenNotFoundException();
        }
        this.tokenRepository.deleteById(id);
    }

    @Transactional
    public void deleteTokensByUserId(UUID id) throws TokenNotFoundException {
        if (!this.tokenRepository.existsByUserId(id)) {
            throw new TokenNotFoundException();
        }

        this.tokenRepository.deleteByUserId(id);
    }
}