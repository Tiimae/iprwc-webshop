package tiimae.webshop.iprwc.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "\"brand\"")
public class Brand {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String brandName;

    @Column(columnDefinition = "TEXT")
    private String webPage;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

    public Brand() { }

    public Brand(String brandName, String webPage, String logoUrl, Set<Product> products) {
        this.brandName = brandName;
        this.webPage = webPage;
        this.logoUrl = logoUrl;
        this.products = products;
    }
}
