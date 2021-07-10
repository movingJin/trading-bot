package movingjin.tradingbot.setting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeSettingForm {
    private String userName;
    private String coinName;
    private String bidReference;
    private Double bidConditionRatio;
    private Double bidQuantity;



    private String askReference;
    private Double askConditionRatio;
}
