package tiimae.webshop.iprwc.DTO;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    
    private String orderId;
    private Date orderDate;
    private String userId;
    private String[] productIds;
    private String[] addressIds;

}
