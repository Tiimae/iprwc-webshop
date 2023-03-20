package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String firstName;

    @Column(columnDefinition = "TEXT")
    private String middleName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String lastName;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    @JsonIgnore
    private String password;

    @NotNull
    @JsonIgnore
    private Boolean verified;

    @NotNull
    @JsonIgnore
    private Boolean reset_required;

    @NotNull
    private Boolean deleted = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private Set<UserAddress> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonBackReference
    @JsonIgnore
    @JsonIgnoreProperties("user")
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "userroles",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_token", referencedColumnName = "id")
    @JsonIgnore
    private Token accessToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token", referencedColumnName = "id")
    @JsonIgnore
    private Token refreshToken;

    public User (UUID id) {
        this.setId(id);
    }
}
