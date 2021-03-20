package movingjin.tradingbot.setting.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class BidTradeSetting {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idx;
    private String userName;
    private String coinName;
    private String candle;
    @Enumerated(EnumType.STRING)
    private Reference reference;
    private Double conditionRatio;
    private Double price;

    public BidTradeSetting.Candle getCandle() {
        return BidTradeSetting.Candle.parse(candle);
    }

    public void setCandle(BidTradeSetting.Candle candle) {
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

        public static BidTradeSetting.Candle parse(String param) {
            BidTradeSetting.Candle candle = null; // Default
            for (BidTradeSetting.Candle item : BidTradeSetting.Candle.values()) {
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
        PROFIT
    }
}
