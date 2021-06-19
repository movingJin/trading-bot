package movingjin.tradingbot.home.controller;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import movingjin.tradingbot.trading.service.TradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final CoinService coinService;
    private final TradeSettingService tradeSettingService;
    private final TradingService tradingService;
    HashMap<String, Future<Integer> > bidThreads = null;

    @Autowired
    public HomeController (CoinService coinService, TradeSettingService tradeSettingService, TradingService tradingService)
    {
        this.coinService = coinService;
        this.tradeSettingService = tradeSettingService;
        this.tradingService = tradingService;
        this.bidThreads = new HashMap<>();
    }

    @GetMapping("/")
    public String home(Model model)
    {
        List<Coin> coins = null;
        Double balance = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        String password = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
            password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
            coins = coinService.getCoinInfo(userName, password);
            balance = coinService.getBalance(userName, password);

            for (Coin coin : coins) {
                BidTradeSetting bid_settings = tradeSettingService.getBidSetting(userName, coin.getName());
                AskTradeSetting ask_settings = tradeSettingService.getAskSetting(userName, coin.getName());
                Double bidQuantity = bid_settings.getQuantity();

                if (bidQuantity < 0) {
                    coin.setIsRun(Coin.AutoRun.UNAVAILABLE);
                }
            }
        }
        model.addAttribute("balance", balance);
        model.addAttribute("coins", coins);

        return "home";
    }

    @RequestMapping(value="/run_or_stop", method= RequestMethod.POST)
    public String auto_run(Model model, @RequestParam("btn") String _idx)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        String password = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
            password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

            int idx = Integer.parseInt(_idx);
            String coinName = Coin.Token.values()[idx].name();
            Coin coin = coinService.getCoinInfo(userName, password, coinName);
            Coin.AutoRun isRun = coin.getIsRun();
            isRun = (isRun == Coin.AutoRun.RUN) ? Coin.AutoRun.STOP : Coin.AutoRun.RUN;

            if(isRun == Coin.AutoRun.RUN)
            {
                Future<Integer> bidThread = tradingService.onTrading(userName, password, coin);
                bidThreads.put(coinName, bidThread);
            }
            else
            {
                Future<Integer> bidThread = bidThreads.get(coinName);
                if (bidThread.isDone()) {
                    try {
                        bidThread.get();
                    } catch(Exception e) {
                        logger.error(e.toString());
                    }
                }
                boolean ret = bidThread.cancel(true);
                logger.info("ret: " + ret);
            }
            coin.setIsRun(isRun);
        }
        return "redirect:/";
    }
}
