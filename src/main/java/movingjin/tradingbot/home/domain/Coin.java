package movingjin.tradingbot.home.domain;

import org.springframework.boot.autoconfigure.web.WebProperties;

public class Coin {
    private String name;
    private Double marketPrice;
    private Double bidPrice;
    private Double quantity;
    private Double income;
    private boolean isRun;

    public Coin ()
    {

    }
    public Coin(String name, Double marketPrice, Double bidPrice, Double quantity)
    {
        this.name = name;
        this.marketPrice = marketPrice;
        this.bidPrice = bidPrice;
        this.quantity = quantity;
        this.income = marketPrice * quantity - bidPrice * quantity;
        this.isRun = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public boolean getIsRun() {
        return isRun;
    }

    public void setIsRun(boolean run) {
        isRun = run;
    }
}
