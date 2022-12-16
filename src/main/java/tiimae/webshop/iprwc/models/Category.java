package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "\"category\"")
public class Category {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

    public Category() { }

    public Category(String categoryName, Set<Product> products) {
        this.categoryName = categoryName;
        this.products = products;
    }
}
