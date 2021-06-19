package movingjin.tradingbot.setting.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "ask_trade_setting")
public class AskTradeSetting {
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

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public enum Reference {
        PROFIT;
    }
}