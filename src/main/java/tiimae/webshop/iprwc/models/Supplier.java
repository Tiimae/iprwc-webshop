package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"supplier\"")
public class Supplier extends BaseEntity {

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String city;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String country;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

}
