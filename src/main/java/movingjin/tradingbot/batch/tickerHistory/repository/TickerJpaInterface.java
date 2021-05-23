package movingjin.tradingbot.batch.tickerHistory.repository;

import movingjin.tradingbot.batch.tickerHistory.domain.Ticker;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface TickerJpaInterface extends CrudRepository<Ticker, Long> {

    List<Ticker> findByCoinNameAndTimeStampBefore(String userName, LocalDateTime days);

    List<Ticker> findByCoinNameAndTimeStampAfter(String userName, LocalDateTime days);
    //@Modifying
    //@Query("delete from TICKER_HISTORY t where t.idx = ?1")
    void deleteByIdx(Long idx);

}
