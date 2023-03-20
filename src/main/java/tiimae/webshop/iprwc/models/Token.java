package tiimae.webshop.iprwc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tiimae.webshop.iprwc.constants.TokenType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "jwtToken")
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseEntity {

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String value;

    @Column(nullable = false)
    private TokenType type;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @OneToOne
    private User user;

    public Token(UUID id) {
        this.setId(id);
    }

    public boolean hasExpired() {
        return expiresAt.compareTo(Instant.now()) < 0;
    }

}
