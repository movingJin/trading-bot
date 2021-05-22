package movingjin.tradingbot.setting.service;

import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.repository.AskTradeSettingJpaInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingJpaInterface;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Optional<BidTradeSetting> getBidSetting(String userName, String coinName)
    {
        return bidRepository.findByUserNameAndCoinName(userName, coinName);
    }

    public Optional<AskTradeSetting> getAskSetting(String userName, String coinName)
    {
        return askRepository.findByUserNameAndCoinName(userName, coinName);
    }
}
