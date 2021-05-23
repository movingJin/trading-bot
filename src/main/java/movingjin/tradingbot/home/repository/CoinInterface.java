package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.home.domain.Coin;

import java.util.List;
import java.util.Optional;

public interface CoinInterface {
    public Double getBalance(String connectKey, String secretKey);
    public Double getCurrentPriceByCoin(String coinName);
    public List<Coin> findAll(String connectKey, String secretKey);
    public Optional<Coin> findByCoinName(String connectKey, String secretKey, String coinName);
}
