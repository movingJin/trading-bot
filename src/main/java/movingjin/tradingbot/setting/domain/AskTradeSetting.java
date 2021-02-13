package movingjin.tradingbot.setting.domain;

import javax.persistence.*;

@Entity(name = "ask_trade_setting")
public class AskTradeSetting {
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

    @Id
    private Long id;

    //@Column(name = "coin_name")
    private String coinName;
    private String candle;
    @Enumerated(EnumType.STRING)
    private Reference reference;
    private Double condition_ratio;
    private Double price;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

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

    public Double getCondition_ratio() {
        return condition_ratio;
    }

    public void setCondition_ratio(Double condition_ratio) {
        this.condition_ratio = condition_ratio;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}