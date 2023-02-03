package tiimae.webshop.iprwc.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.TABLE)
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

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
    private Boolean verified;

    @NotNull
    @JsonIgnore
    private Boolean reset_required;

    @NotNull
    private Boolean deleted = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private Set<UserAddress> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
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
        this.id = id;
    }

    public User(String firstName, String middleName, String lastName, String email, String password, boolean verified, boolean reset_required, Set<UserAddress> addresses, Set<Order> orders, Set<Role> roles) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.reset_required = reset_required;
        this.verified = verified;
        this.addresses = addresses;
        this.orders = orders;

        for (Role role : roles) {
            this.addRole(role);
        }
    }

    public void addRole(Role role) {
        this.getRoles().add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.getRoles().remove(role);
        role.getUsers().remove(this);
    }
}
