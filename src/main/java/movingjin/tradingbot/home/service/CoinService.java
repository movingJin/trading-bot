package movingjin.tradingbot.home.service;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.repository.CoinInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CoinService{
    private CoinInterface coinRepository;

    @Autowired
    public CoinService(CoinInterface coinInterface)
    {
        this.coinRepository = coinInterface;
    }

    public List<Coin> getCoinInfo(String connectKey, String secretKey) {
        return coinRepository.findAll(connectKey, secretKey);
    }

    public Coin getCoinInfo(String connectKey, String secretKey, String coinName) {
        return coinRepository.findByCoinName(connectKey, secretKey, coinName).get();
    }

    public Double getBalance(String connectKey, String secretKey) {
        return coinRepository.getBalance(connectKey, secretKey);
    }

    public Double getCurrentPrice(String coinName)
    {
        return coinRepository.getCurrentPriceByCoin(coinName);
    }
}
