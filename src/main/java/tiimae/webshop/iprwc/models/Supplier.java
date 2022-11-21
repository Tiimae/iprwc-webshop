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
@Table(name = "\"supplier\"")
public class Supplier {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();

    public Supplier() { }

    public Supplier(String name, String address, String zipcode, String city, String country, Set<Product> products) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.products = products;
    }
}
