package movingjin.tradingbot.trading;

import lombok.RequiredArgsConstructor;
import movingjin.tradingbot.batch.tickerHistory.service.TickerHistoryService;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class TradingService {
    private final CoinService coinService;
    private final TradeSettingService tradeSettingService;
    private final TickerHistoryService tickerHistoryService;
    Logger logger = LoggerFactory.getLogger(getClass());
    private boolean doIt = true;

    @Async
    public Future<Integer> onTrading(String userName, Coin coin) {
        int u = 0;
        BidTradeSetting bidTradeSetting = tradeSettingService.getBidSetting(userName, coin.getName());
        AskTradeSetting askTradeSetting = tradeSettingService.getAskSetting(userName, coin.getName());
        try {
            while (!Thread.interrupted()) {
                Double currentPrice = coinService.getCurrentPrice(coin.getName());
                Double bidMa = tickerHistoryService.getMovingAverage(coin.getName(), (long) bidTradeSetting.getReference().getValue());
                Double askMa = tickerHistoryService.getMovingAverage(coin.getName(), (long) askTradeSetting.getReference().getValue());
                logger.info(coin.getName() + ", bidMa: " + bidMa + ", currentPrice: " + currentPrice);
                //Double askMa = tickerHistoryService.getMovingAverage(coin.getName(), (long) askTradeSetting.getReference().getValue());

                if(currentPrice < bidMa)
                {
                    logger.info(coin.getName() + " buy at " + currentPrice);
                }

                if(currentPrice > askMa)
                {
                    logger.info(coin.getName() + " sell at " + currentPrice);
                }
                Thread.sleep((long) 1000);
            }
        }
        catch (InterruptedException e) {
            logger.error(e.toString());
        }

        return new AsyncResult<>(0);
    }

    public void setDoIt(boolean doIt)
    {
        this.doIt = doIt;
    }

    public boolean getDoIt()
    {
        return doIt;
    }
}
