package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.BidTradeSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BidTradeSettingJpaInterface extends JpaRepository<BidTradeSetting, Long> {
    Optional<BidTradeSetting> findByUserName(String userName);

    boolean existsByUserName(@Param("user_name") String userName);

    Optional<BidTradeSetting> findByUserNameAndCoinName(String userName, String coinName);
}
