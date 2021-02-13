package movingjin.tradingbot.setting.service;

import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.repository.AskTradeSettingInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingInterface;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class TradeSettingService {
    private final BidTradeSettingInterface bidRepository;
    private final AskTradeSettingInterface askRepository;
    public TradeSettingService(BidTradeSettingInterface bidRepository, AskTradeSettingInterface askRepository)
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

    public Optional<BidTradeSetting> getBidSetting(String coinName)
    {
        return bidRepository.findByCoinName(coinName);
    }

    public Optional<AskTradeSetting> getAskSetting(String coinName)
    {
        return askRepository.findByCoinName(coinName);
    }
}
