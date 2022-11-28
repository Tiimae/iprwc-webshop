package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.TABLE)
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<UserAddress> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "userroles",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Set<Role> roles = new HashSet<>();

    public User() { }

    public User(String firstName, String middleName, String lastName, String email, String password, Set<UserAddress> addresses, Set<Order> orders, Set<Role> roles) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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
