package movingjin.tradingbot.setting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AskTradeSettingDTO {
    private String reference;
    private Double conditionRatio;
}