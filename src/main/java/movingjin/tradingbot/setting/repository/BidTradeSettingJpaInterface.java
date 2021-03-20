package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.BidTradeSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BidTradeSettingJpaInterface extends JpaRepository<BidTradeSetting, Long>, BidTradeSettingInterface {
    @Override
    Optional<BidTradeSetting> findByUserName(String userName);

    @Override
    boolean existsByUserName(@Param("user_name") String userName);

    @Override
    Optional<BidTradeSetting> findByUserNameAndCoinName(String userName, String coinName);
}
