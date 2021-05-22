package movingjin.tradingbot.batch.tickerHistory.repository;

import movingjin.tradingbot.batch.tickerHistory.domain.Ticker;

import java.time.LocalDateTime;
import java.util.List;


public interface TickerInterface {
    Ticker save(Ticker ticker);
    List<Ticker> findByCoinNameAndTimeStampBefore(String coinName, LocalDateTime days);
    List<Ticker> findAll();
    //@Transactional
    //@Modifying
    //@Query("delete from TICKER_HISTORY t where t.idx = ?1")
    void deleteByIdx(Long idx);

}
