package movingjin.tradingbot.batch.tickerHistory;

import lombok.RequiredArgsConstructor;
import movingjin.tradingbot.batch.tickerHistory.domain.Ticker;
import movingjin.tradingbot.batch.tickerHistory.repository.TickerJpaInterface;
import movingjin.tradingbot.home.domain.Coin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service("jobExecutor")
@Transactional("transactionManager")
@RequiredArgsConstructor
public class JobExecutorImpl {
    @Autowired
    private final TickerJpaInterface tickerRepository;

    static Logger log = Logger.getLogger(JobExecutorImpl.class.getName());

    public void execute() {
        log.info("transaction exists? ".concat( String.valueOf(TransactionSynchronizationManager.isActualTransactionActive()) )); // true
        log.info("transaction sync? ".concat( String.valueOf(TransactionSynchronizationManager.isSynchronizationActive()) ));  // true


        for(Coin.Token token: Coin.Token.values()) {
            List<Ticker> tickers = tickerRepository.findByCoinNameAndTimeStampBefore(token.name(), LocalDateTime.now().minusDays(120));
            log.info("======= Start ticker history delete step =======");
            for(Ticker ticker: tickers)
            {
                log.info(ticker.getTimeStamp().toString() + "fetch and deleting: " + ticker.getIdx());
                //tickerRepository.delete(ticker);
                ticker.setVolume(777.0);
                tickerRepository.save(ticker);
            }

            log.info("======= Start ticker history insert step =======");
            Ticker ticker = new Ticker(token.name());
            tickerRepository.save(ticker);
        }
        return;
    }
}
