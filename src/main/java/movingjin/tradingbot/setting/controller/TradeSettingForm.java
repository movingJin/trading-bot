package movingjin.tradingbot.setting.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeSettingForm {
    private String userName;
    private String coinName;
    private String bidCandle;
    private String bidReference;
    private Double bidConditionRatio;
    private Double bidPrice;



    private String askCandle;
    private String askReference;
    private Double askConditionRatio;
    private Double askPrice;
}
