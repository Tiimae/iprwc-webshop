package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "useraddress")
public class UserAddress {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String street;

    @Column(nullable = false)
    private long houseNumber;

    private String addition;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String zipcode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String city;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String country;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

    @ManyToMany(mappedBy = "userAddresses", cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("userAddresses")
    private Set<Order> orders = new HashSet<>();

    public UserAddress() { }

    public UserAddress(String street, long houseNumber, String addition, String zipcode, String city, String country, String type, User user, Set<Order> orders) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.addition = addition;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.type = type;
        this.user = user;
        this.orders = orders;
    }
}
