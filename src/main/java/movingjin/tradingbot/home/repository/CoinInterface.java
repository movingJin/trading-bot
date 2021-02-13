package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.home.domain.Coin;

import java.util.List;

public interface CoinInterface {
    public List<Coin> findAll();
}
