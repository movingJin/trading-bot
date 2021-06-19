package movingjin.tradingbot.setting.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "bid_trade_setting")
public class BidTradeSetting {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "coin_name")
    private String coinName;
    @Enumerated(EnumType.STRING)
    private Reference reference;
    @Column(name = "condition_ratio")
    private Double conditionRatio;
    private Double quantity;

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public enum Reference {
        MA5(5),
        MA20(20),
        MA60(60),
        MA120(120);

        private int value;
        Reference(int value)
        {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
