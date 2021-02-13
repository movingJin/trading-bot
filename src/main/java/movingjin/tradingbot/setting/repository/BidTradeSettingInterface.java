package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.BidTradeSetting;

import java.util.List;
import java.util.Optional;

public interface BidTradeSettingInterface {
    BidTradeSetting save(BidTradeSetting bidTradeSetting);
    Optional<BidTradeSetting> findByCoinName(String coinName);
    List<BidTradeSetting> findAll();
}
