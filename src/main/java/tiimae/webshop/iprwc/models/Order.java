package tiimae.webshop.iprwc.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private UUID id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private Date orderDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OrderProduct> OrderProducts = new HashSet<>();

    public Order() { }

    public Order(String orderId, Date orderDate, User user, Set<OrderProduct> orderProducts) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.user = user;
        OrderProducts = orderProducts;
    }
}
