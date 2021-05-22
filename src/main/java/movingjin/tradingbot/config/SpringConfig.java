package movingjin.tradingbot.config;

import lombok.RequiredArgsConstructor;
import movingjin.tradingbot.batch.tickerHistory.JobExecutorImpl;
import movingjin.tradingbot.batch.tickerHistory.repository.TickerJpaInterface;
import movingjin.tradingbot.home.repository.APICoinRepository;
import movingjin.tradingbot.home.repository.CoinInterface;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.repository.AskTradeSettingJpaInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingJpaInterface;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final BidTradeSettingJpaInterface bidTradeSettingInterface;
    private final AskTradeSettingJpaInterface askTradeSettingInterface;
    private final TickerJpaInterface tickerRepository;

    @Bean
    public CoinService coinService()
    {
        //return new CoinService(coinInterface);
        return new CoinService(coinRepository());
    }
    @Bean
    public CoinInterface coinRepository()
    {
        return new APICoinRepository();
    }

    @Bean
    public TradeSettingService tradeSettingService()
    {
        return new TradeSettingService(bidTradeSettingInterface, askTradeSettingInterface);
    }

    @Bean
    public JobExecutorImpl jobExecutorImpl()
    {
        return new JobExecutorImpl(tickerRepository);
    }
}
