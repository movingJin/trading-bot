package movingjin.tradingbot.home.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coin {
    private String name;
    private Double marketPrice;
    private Double bidPrice;
    private Double quantity;
    private Double income;
    private AutoRun isRun;

    public Coin ()
    {

    }

    public Coin (String name)
    {
        this.name = name;
        this.isRun = AutoRun.STOP;
    }

    public Coin(String name, Double marketPrice, Double bidPrice, Double quantity)
    {
        this.name = name;
        this.marketPrice = marketPrice;
        this.bidPrice = bidPrice;
        this.quantity = quantity;
        this.income = marketPrice * quantity - bidPrice * quantity;
        this.isRun = AutoRun.STOP;
    }

    public enum AutoRun {
        STOP(0), RUN(1), UNAVAILABLE(-1);

        private final int value;
        private AutoRun(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public enum Token {
        BTC,
        ETH,
        XRP
    }
}
