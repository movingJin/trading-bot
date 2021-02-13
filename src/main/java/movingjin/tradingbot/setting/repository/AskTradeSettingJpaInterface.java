package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.AskTradeSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AskTradeSettingJpaInterface extends JpaRepository<AskTradeSetting, Long>, AskTradeSettingInterface {
    @Override
    Optional<AskTradeSetting> findByCoinName(String coinName);
}
