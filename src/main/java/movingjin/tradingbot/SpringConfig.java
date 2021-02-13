package movingjin.tradingbot;

import movingjin.tradingbot.home.repository.APICoinRepository;
import movingjin.tradingbot.setting.repository.AskTradeSettingInterface;
import movingjin.tradingbot.home.repository.CoinInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingInterface;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final BidTradeSettingInterface bidTradeSettingInterface;
    private final AskTradeSettingInterface askTradeSettingInterface;

    @Autowired
    public SpringConfig(BidTradeSettingInterface bidTradeSettingInterface, AskTradeSettingInterface askTradeSettingInterface)
    {
        this.bidTradeSettingInterface = bidTradeSettingInterface;
        this.askTradeSettingInterface = askTradeSettingInterface;
    }
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
}
