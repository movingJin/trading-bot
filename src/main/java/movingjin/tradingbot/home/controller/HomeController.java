package movingjin.tradingbot.home.controller;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private final CoinService coinService;
    private final TradeSettingService tradeSettingService;

    @Autowired
    public HomeController (CoinService coinService, TradeSettingService tradeSettingService)
    {
        this.coinService = coinService;
        this.tradeSettingService = tradeSettingService;
    }

    @GetMapping("/")
    public String home(Model model)
    {
        List<Coin> coins = coinService.getCoinInfo();
        for(Coin coin: coins)
        {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName = null;
            if (principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            } else {
                userName = principal.toString();
            }
            BidTradeSetting bid_settings = tradeSettingService.getBidSetting(userName, coin.getName()).get();
            AskTradeSetting ask_settings = tradeSettingService.getAskSetting(userName, coin.getName()).get();
            Double bidPrice = bid_settings.getPrice();
            Double askPrice = ask_settings.getPrice();

            if (bidPrice < 0 || askPrice < 0)
            {
                coin.setIsRun(Coin.AutoRun.UNAVAILABLE);
            }
        }
        model.addAttribute("coins", coins);

        return "home";
    }

    @RequestMapping(value="/run_or_stop", method= RequestMethod.POST)
    public String auto_run(Model model, @RequestParam("btn") String _idx)
    {
        List<Coin> coins = coinService.getCoinInfo();
        int idx = Integer.parseInt(_idx);
        Coin.AutoRun isRun = coins.get(idx).getIsRun();
        isRun = (isRun == Coin.AutoRun.RUN) ? Coin.AutoRun.STOP: Coin.AutoRun.RUN;
        coins.get(idx).setIsRun(isRun);

        return "redirect:/";
    }
}
