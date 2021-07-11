package movingjin.tradingbot.trading.service;

import lombok.RequiredArgsConstructor;
import movingjin.tradingbot.batch.tickerHistory.service.TickerHistoryService;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import movingjin.tradingbot.trading.domain.Order;
import movingjin.tradingbot.trading.repository.OrderJpaInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.DoubleStream;

@Service
@RequiredArgsConstructor
public class TradingService {
    private static final int MAX_RETRY_GET_ORDER_COUNT = 10;
    private final OrderJpaInterface orderJpaInterface;
    private final CoinService coinService;
    private final TradeSettingService tradeSettingService;
    private final TickerHistoryService tickerHistoryService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Async
    public Future<Integer> onTrading(String userName, String password, Coin coin) {
        BidTradeSetting bidTradeSetting = tradeSettingService.getBidSetting(userName, coin.getName());
        AskTradeSetting askTradeSetting = tradeSettingService.getAskSetting(userName, coin.getName());
        try {
            while (true) {
                if(Thread.currentThread().isInterrupted())
                {
                    logger.info(String.format("%s stop %s's auto trading.", userName, coin.getName()));
                    return new AsyncResult<>(1);
                }
                Double currentPrice = coinService.getCurrentPrice(coin.getName());
                Double movingAverage = tickerHistoryService.getMovingAverage(coin.getName(), (long) bidTradeSetting.getReference().getValue());
                Double conditionRatio = bidTradeSetting.getConditionRatio();
                Double biddingCondition = movingAverage * (conditionRatio / 100.0);
                AskTradeSetting.Reference askReference = askTradeSetting.getReference();
                logger.info(coin.getName() + ", biddingCondition: " + biddingCondition + ", currentPrice: " + currentPrice);

                if(currentPrice < biddingCondition)
                {
                    Double balance = coinService.getBalance(userName, password);
                    Double quantity = bidTradeSetting.getQuantity();
                    Double totalPrice = currentPrice * quantity;

                    if(totalPrice < balance)
                    {
                        String order_id = coinService.tryBid(userName, password, coin.getName(), quantity);
                        logger.info(coin.getName() + " buy at " + currentPrice + ", order_id: " + order_id);

                        Order order = coinService.getOrders(userName, password, coin.getName(), order_id);
                        //Order order = coinService.getOrders(userName, password, "XRP", "C0106000000257717763");
                        for(int tryCount=0 ;order == null && tryCount < MAX_RETRY_GET_ORDER_COUNT; tryCount++)
                        {
                            order = coinService.getOrders(userName, password, coin.getName(), order_id);
                            //order = coinService.getOrders(userName, password, coin.getName(), "C0106000000257717763");
                            logger.info("get order detail is failed. retry after 3 seconds later.");
                            Thread.sleep(3000);
                        }
                        orderJpaInterface.save(order);
                    }
                }



                if(askReference == AskTradeSetting.Reference.PROFIT)
                {
                    Double goalProfit = askTradeSetting.getConditionRatio();
                    List<Order> orders= orderJpaInterface.findByUserNameAndCoinNameAndIsHoldTrue(userName, coin.getName());
                    if(orders.size() > 0)
                    {
                        Double quantity = orders.stream()
                                .flatMapToDouble(qt -> DoubleStream.of(qt.getQuantity()))
                                .sum();
                        Double ordered_price = orders.stream()
                                .flatMapToDouble(price -> DoubleStream.of(price.getPrice()))
                                .average().getAsDouble();
                        Double profit = (currentPrice / ordered_price) * 100.0 - 100.0;

                        logger.info(coin.getName() + ", goalProfit: " + goalProfit + ", profit: " + profit);
                        if(profit > goalProfit)
                        {
                            String order_id = coinService.trySell(userName, password, coin.getName(), quantity);
                            logger.info(coin.getName() + " sell at " + currentPrice + ", order_id: " + order_id);

                            orders.forEach(order -> order.setIsHold(false));
                            orderJpaInterface.saveAll(orders);
                        }
                    }
                }
                Thread.sleep((long) 1000);
            }
        }
        catch (InterruptedException e) {
            logger.error(e.toString());
        }

        return new AsyncResult<>(0);
    }
}
