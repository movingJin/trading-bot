package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.AskTradeSetting;

import java.util.List;
import java.util.Optional;

public interface AskTradeSettingInterface {
    AskTradeSetting save(AskTradeSetting bidTradeSetting);
    Optional<AskTradeSetting> findByCoinName(String coinName);
    List<AskTradeSetting> findAll();
}
