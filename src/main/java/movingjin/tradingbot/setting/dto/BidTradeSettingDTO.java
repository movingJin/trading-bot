package movingjin.tradingbot.setting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidTradeSettingDTO {
    private String reference;
    private Double conditionRatio;
    private Double quantity;
    private Double price;
}
