package movingjin.tradingbot.trading.service;

import movingjin.tradingbot.batch.tickerHistory.domain.Ticker;
import movingjin.tradingbot.batch.tickerHistory.repository.TickerJpaInterface;
import movingjin.tradingbot.batch.tickerHistory.service.TickerHistoryService;
import movingjin.tradingbot.home.repository.APICoinRepositoryTest;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import movingjin.tradingbot.trading.domain.Order;
import movingjin.tradingbot.trading.repository.OrderJpaInterface;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.DoubleStream;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TradingServiceTest {

    @Autowired
    private TickerHistoryService tickerHistoryService;
    @Autowired
    private TradeSettingService tradeSettingService;
    @Autowired
    private OrderJpaInterface orderJpaInterface;
    private APICoinRepositoryTest apiCoinRepositoryTest;
    @Autowired
    private TickerJpaInterface tickerRepository;
    Logger logger = LoggerFactory.getLogger(getClass());

    private String userName = "test_key";
    private String coinName = "ETH";

    TradingServiceTest()
    {
        apiCoinRepositoryTest = new APICoinRepositoryTest();
    }

    @Test
    void onTradingTest() {
        //given
        long maPeriod = 60;
        Double conditionRatio = 90.0;
        Double bidQuantity = 5.0;
        Double goalProfit = 5.0;

        Double gross_profit = 0.0;
        Double remain_coins = 0.0;
        Double average_bidding_price = 0.0;

        //when
        List<Ticker> tickers = tickerRepository.findByCoinNameAndTimeStampAfter(coinName, LocalDateTime.now().minusDays(180));
        try {
            for (int u= (int) maPeriod; u<tickers.size(); u++) {
                Double currentPrice = tickers.get(u).getClose();
                Double movingAverage;
                Double sum = 0.0;
                for(int v=u-(int)maPeriod; v<u; v++)
                {
                    sum += tickers.get(v).getClose();
                }
                movingAverage = sum / maPeriod;
                Double biddingCondition = movingAverage * (conditionRatio / 100.0);
                AskTradeSetting.Reference askReference = AskTradeSetting.Reference.PROFIT;
                logger.info(coinName + ", biddingCondition: " + biddingCondition + ", currentPrice: " + currentPrice);

                if(currentPrice < biddingCondition)
                {
                    Double balance = apiCoinRepositoryTest.getBalance();

                    Double totalPrice = currentPrice * bidQuantity;

                    if(totalPrice < balance)
                    {
                        String order_id = apiCoinRepositoryTest.marketBidding(coinName, currentPrice, bidQuantity);
                        logger.info(coinName + " buy at " + currentPrice + ", order_id: " + order_id);

                        Order order = apiCoinRepositoryTest.getOrderDetail(coinName, order_id, currentPrice, bidQuantity);
                        orderJpaInterface.save(order);
                    }
                }


                if(askReference == AskTradeSetting.Reference.PROFIT)
                {
                    List<Order> orders= orderJpaInterface.findByUserNameAndCoinNameAndIsHoldTrue(userName, coinName);
                    if(orders.size() > 0)
                    {
                        Double quantity = orders.stream()
                                .flatMapToDouble(qt -> DoubleStream.of(qt.getQuantity()))
                                .sum();
                        Double ordered_price = orders.stream()
                                .flatMapToDouble(price -> DoubleStream.of(price.getPrice()))
                                .average().getAsDouble();
                        Double profit = (currentPrice / ordered_price) * 100.0 - 100.0;

                        logger.info(coinName + ", goalProfit: " + goalProfit + ", profit: " + profit);
                        if(profit > goalProfit)
                        {
                            gross_profit += (currentPrice * quantity) - (ordered_price * quantity);
                            String order_id = apiCoinRepositoryTest.marketSell(coinName, currentPrice, quantity);
                            logger.info(coinName + " sell at " + currentPrice + ", order_id: " + order_id);

                            orders.forEach(order -> order.setIsHold(false));
                            orderJpaInterface.saveAll(orders);
                        }

                        remain_coins = quantity;
                        average_bidding_price = ordered_price;
                    }
                    else
                    {
                        remain_coins = 0.0;
                        average_bidding_price = 0.0;
                    }
                }
                Thread.sleep((long) 100);
            }
        }
        catch (InterruptedException e) {
            logger.error(e.toString());
        }

        //then
        Double current_price = tickers.get(tickers.size()-1).getClose();
        Double net_profit = gross_profit - ((average_bidding_price-current_price) * remain_coins);
        String result = String.format("remain: {counts : %f, average bidding_price: %f}, current rice: %f, gross profit: %f, net profit: %f", remain_coins, average_bidding_price, current_price, gross_profit, net_profit);
        logger.info(result);
    }
}