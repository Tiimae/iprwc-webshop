package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "\"role\"")
public class Role {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("roles")
    private Set<User> users = new HashSet<>();

    public Role() { }

    public Role(String name, Set<User> users) {
        this.name = name;

        for (User user : users) {
            this.addUser(user);
        }
    }

    public void addUser(User user) {
        this.getUsers().add(user);
        user.getRoles().add(this);
    }

    public void removeUser(User user) {
        this.getUsers().remove(user);
        user.getRoles().remove(this);
    }
}
