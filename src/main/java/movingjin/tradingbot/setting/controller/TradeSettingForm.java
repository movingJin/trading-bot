package movingjin.tradingbot.setting.controller;

public class TradeSettingForm {
    private String coinName;
    private String bid_candle;
    private String bid_reference;
    private Double bid_condition_ratio;
    private Double bid_price;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getBidCandle() {
        return bid_candle;
    }

    public void setBidCandle(String candle) {
        this.bid_candle = candle;
    }

    public String getBidReference() {
        return bid_reference;
    }

    public void setBidReference(String reference) {
        this.bid_reference = reference;
    }

    public Double getBidCondition_ratio() {
        return bid_condition_ratio;
    }

    public void setBidCondition_ratio(Double condition_ratio) {
        this.bid_condition_ratio = condition_ratio;
    }

    public Double getBidPrice() {
        return bid_price;
    }

    public void setBidPrice(Double price) {
        this.bid_price = price;
    }



    private String ask_candle;
    private String ask_reference;
    private Double ask_condition_ratio;
    private Double ask_price;

    public String getAskCandle() {
        return ask_candle;
    }

    public void setAskCandle(String candle) {
        this.ask_candle = candle;
    }

    public String getAskReference() {
        return ask_reference;
    }

    public void setAskReference(String reference) {
        this.ask_reference = reference;
    }

    public Double getAskCondition_ratio() {
        return ask_condition_ratio;
    }

    public void setAskCondition_ratio(Double condition_ratio) {
        this.ask_condition_ratio = condition_ratio;
    }

    public Double getAskPrice() {
        return ask_price;
    }

    public void setAskPrice(Double price) {
        this.ask_price = price;
    }
}
