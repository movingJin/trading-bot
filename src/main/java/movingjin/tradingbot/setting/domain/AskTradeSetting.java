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
    private String candle;
    @Enumerated(EnumType.STRING)
    private Reference reference;
    @Column(name = "condition_ratio")
    private Double conditionRatio;
    private Double price;

    public Candle getCandle() {
        return Candle.parse(candle);
    }

    public void setCandle(Candle candle) {
        this.candle = candle.getValue();
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }




    public enum Candle {
        MINUTE_5("5 Minutes"),
        MINUTE_10("10 Minutes"),
        HALF_HOUR("Half hour"),
        HOUR("1 hour"),
        DAY("1 Day");

        private String value;

        Candle(String value) { this.value = value; }

        public String getValue() { return value; }

        public static Candle parse(String param) {
            Candle candle = null; // Default
            for (Candle item : Candle.values()) {
                if (item.getValue().equals(param) ) {
                    candle = item;
                    break;
                }
            }
            return candle;
        }
    }

    public enum Reference {
        MA5,
        MA20,
        MA60,
        MA120,
        PROFIT;
    }
}