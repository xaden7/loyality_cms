package md.akdev.loyality_cms.model.order1c;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID uid;
    private String nr;
    private LocalDateTime date;
    private String status;
    private BigDecimal sum;
    private String address;
    private String deliveryType;
    private String phone;
    private String branchCode;
    private List<OrderRow> products;
}
