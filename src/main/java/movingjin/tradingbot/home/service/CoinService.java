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

    public List<Coin> getCoinInfo() {
        return coinRepository.findAll();
    }

}
