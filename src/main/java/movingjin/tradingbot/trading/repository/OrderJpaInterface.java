package movingjin.tradingbot.trading.repository;

import movingjin.tradingbot.trading.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaInterface extends JpaRepository<Order, Long> {
    List<Order> findByUserNameAndCoinName(String userName, String coinName);
}
