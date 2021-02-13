package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.BidTradeSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidTradeSettingJpaInterface extends JpaRepository<BidTradeSetting, Long>, BidTradeSettingInterface {
    @Override
    Optional<BidTradeSetting> findByCoinName(String coinName);
}
