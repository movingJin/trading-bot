package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.home.domain.Coin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APICoinRepository implements CoinInterface {
    private static final Map<String, Coin> store = new HashMap<>();

    public APICoinRepository()
    {
        Coin btc = new Coin("BTC", 36000000.0, 12500000.0, 1.0);
        Coin eth = new Coin("ETH", 1100000.0, 210000.0, 10.0);
        Coin xrp = new Coin("XRP", 4000.0, 505.0, 25000.0);

        store.put("BTC", btc);
        store.put("ETH", eth);
        store.put("XRP", xrp);
    }

    @Override
    public List<Coin> findAll() {
        return new ArrayList<>(store.values());
    }
}
