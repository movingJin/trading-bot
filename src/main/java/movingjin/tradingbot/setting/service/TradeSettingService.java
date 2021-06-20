package movingjin.tradingbot.setting.service;

import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.repository.AskTradeSettingJpaInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingJpaInterface;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TradeSettingService {
    private final BidTradeSettingJpaInterface bidRepository;
    private final AskTradeSettingJpaInterface askRepository;

    public TradeSettingService(BidTradeSettingJpaInterface bidRepository, AskTradeSettingJpaInterface askRepository)
    {
        this.bidRepository = bidRepository;
        this.askRepository = askRepository;
    }

    public int save(BidTradeSetting bidTradeSetting, AskTradeSetting askTradeSetting)
    {
        bidRepository.save(bidTradeSetting);
        askRepository.save(askTradeSetting);
        return 0;
    }

    public BidTradeSetting getBidSetting(String userName, String coinName)
    {
        return bidRepository.findByUserNameAndCoinName(userName, coinName).get();
    }

    public AskTradeSetting getAskSetting(String userName, String coinName)
    {
        return askRepository.findByUserNameAndCoinName(userName, coinName).get();
    }
}
