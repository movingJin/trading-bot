package movingjin.tradingbot.trading;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class TradingService {
    Logger logger = LoggerFactory.getLogger(getClass());
    private boolean doIt = true;

    @Async
    public Future<Integer> onBidding(Coin coin, BidTradeSetting bidTradeSetting) {
        int u = 0;
        try {
            while (!Thread.interrupted()) {
                logger.info(coin.getName() + ": thread" + u++);
                Thread.sleep((long) 10);
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
