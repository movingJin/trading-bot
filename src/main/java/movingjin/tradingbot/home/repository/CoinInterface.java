package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.trading.domain.Order;

import java.util.List;
import java.util.Optional;

public interface CoinInterface {
    public Double getBalance(String connectKey, String secretKey);
    public Double getCurrentPriceByCoin(String coinName);
    public List<Coin> findAll(String connectKey, String secretKey);
    public Optional<Coin> findByCoinName(String connectKey, String secretKey, String coinName);
    public String marketBidding(String connectKey, String secretKey, String coinName, Double quantity);
    public String marketSell(String connectKey, String secretKey, String coinName, Double quantity);
    public Order getOrderDetail(String connectKey, String secretKey, String coinName, String order_id);
}
