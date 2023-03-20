package tiimae.webshop.iprwc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"cart\"")
public class Cart extends BaseEntity {

    @OneToOne
    private User user;

    @ManyToOne
    private Product product;
    private long quantity;

}
