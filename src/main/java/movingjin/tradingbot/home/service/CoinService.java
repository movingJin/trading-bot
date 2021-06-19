package movingjin.tradingbot.home.service;

import lombok.RequiredArgsConstructor;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.repository.CoinInterface;
import movingjin.tradingbot.trading.domain.Order;
import movingjin.tradingbot.trading.repository.OrderJpaInterface;

import java.util.List;
import java.util.stream.DoubleStream;

@RequiredArgsConstructor
public class CoinService{
    private final CoinInterface coinRepository;
    private final OrderJpaInterface orderJpaInterface;

    public List<Coin> getCoinInfo(String userName, String password) {
        List<Coin> coins = coinRepository.findAll(userName, password);
        for(Coin coin: coins)
        {
            List<Order> orders= orderJpaInterface.findByUserNameAndCoinName(userName, coin.getName());
            Double ordered_price = 0.0;
            Double ordered_counts = 0.0;
            if(orders.size() > 0)
            {
                ordered_price = orders.stream()
                        .flatMapToDouble(price -> DoubleStream.of(price.getPrice()))
                        .average().getAsDouble();
                ordered_counts = orders.stream()
                        .flatMapToDouble(count -> DoubleStream.of(count.getQuantity()))
                        .sum();
            }
            Double inCome = coin.getMarketPrice() * ordered_counts - ordered_price * ordered_counts;
            coin.setBidPrice(ordered_price);
            coin.setQuantity(ordered_counts);
            coin.setIncome(inCome);
        }
        return coins;
    }

    public Coin getCoinInfo(String userName, String password, String coinName) {
        Coin coin = coinRepository.findByCoinName(coinName, password, coinName).get();
        List<Order> orders= orderJpaInterface.findByUserNameAndCoinName(userName, coin.getName());
        Double ordered_price = 0.0;
        Double ordered_counts = 0.0;
        if(orders.size() > 0)
        {
            ordered_price = orders.stream()
                    .flatMapToDouble(price -> DoubleStream.of(price.getPrice()))
                    .average().getAsDouble();
            ordered_counts = orders.stream()
                    .flatMapToDouble(count -> DoubleStream.of(count.getQuantity()))
                    .sum();
        }
        Double inCome = coin.getMarketPrice() * ordered_counts - ordered_price * ordered_counts;
        coin.setBidPrice(ordered_price);
        coin.setQuantity(ordered_counts);
        coin.setIncome(inCome);
        return coin;
    }

    public Double getBalance(String userName, String password) {
        return coinRepository.getBalance(userName, password);
    }

    public Double getCurrentPrice(String coinName)
    {
        return coinRepository.getCurrentPriceByCoin(coinName);
    }

    public String getOrderIdOnBidding(String userName, String password, String coinName, Double quantity)
    {
        return coinRepository.marketBidding(userName, password, coinName, quantity);
    }

    public Order getOrders(String userName, String password, String coinName, String order_id)
    {
        return coinRepository.getOrderDetail(userName, password, coinName, order_id);
    }
}
