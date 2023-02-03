package tiimae.webshop.iprwc.models;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.TABLE)
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String orderId;

    @Column(nullable = false)
    private Date orderDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("orders")
    @Nullable
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OrderProduct> orderProducts = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "orderaddresses",
            joinColumns = @JoinColumn(name = "orderid"),
            inverseJoinColumns = @JoinColumn(name = "useraddressid")
    )
    private Set<UserAddress> userAddresses = new HashSet<>();

    public Order() {
    }

    public Order(String orderId, Date orderDate, User user, Set<OrderProduct> orderProducts, Set<UserAddress> userAddresses) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.user = user;
        this.orderProducts = orderProducts;
        this.userAddresses = userAddresses;
    }
}
