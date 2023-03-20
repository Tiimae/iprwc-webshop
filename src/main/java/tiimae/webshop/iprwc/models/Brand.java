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
@Table(name = "\"brand\"")
public class Brand extends BaseEntity {

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String brandName;

    @Column(columnDefinition = "TEXT")
    private String webPage;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Product> products = new HashSet<>();
}
