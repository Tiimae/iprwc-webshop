package tiimae.webshop.iprwc.models;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tiimae.webshop.iprwc.constants.TokenType;

@Entity
@Getter
@Setter
@Table(name = "jwtToken")
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String value;

    @Column(nullable = false)
    private TokenType type;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @OneToOne
    @JsonIgnore
    private User user;

    public Token(UUID id) {
        this.id = id;
    }

    public Token(String value, TokenType type, User user) {
        this.value = value;
        this.type = type;
        this.user = user;
    }

    public Token(String value, TokenType type, Instant issuedAt, Instant expiresAt, User user) {
        this.value = value;
        this.type = type;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public boolean hasExpired() {
        return expiresAt.compareTo(Instant.now()) < 0;
    }

}
