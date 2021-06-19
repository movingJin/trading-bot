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
    @Column(nullable = false, name = "time_stamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timeStamp;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "coin_name")
    private String coinName;
    private Double price;
    private Double quantity;

    public Order(){}
    public Order(LocalDateTime timeStamp, String userName, String coinName, Double price, Double quantity)
    {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.coinName = coinName;
        this.price = price;
        this.quantity = quantity;
    }
}
