package movingjin.tradingbot.trading.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "BIDDING_ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "order_id")
    private String orderId;
    @Column(nullable = false, name = "time_stamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timeStamp;
    @Column(nullable = false, name = "user_name")
    private String userName;
    @Column(nullable = false, name = "coin_name")
    private String coinName;
    private Double price;
    private Double quantity;
    @Column(nullable = false, name = "is_hold", columnDefinition = "boolean")
    private Boolean isHold;

    public Order(){}
    public Order(String orderId, LocalDateTime timeStamp, String userName, String coinName, Double price, Double quantity)
    {
        this.orderId = orderId;
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.coinName = coinName;
        this.price = price;
        this.quantity = quantity;
        this.isHold = true;
    }
}
