package movingjin.tradingbot.setting.controller;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TradeSettingController {
    private final CoinService coinService;
    private final TradeSettingService tradeSettingService;

    @Autowired
    public TradeSettingController(CoinService coinService, TradeSettingService tradeSettingService)
    {
        this.coinService = coinService;
        this.tradeSettingService = tradeSettingService;
    }

    @GetMapping("/settings/edit")
    public String editSetting(Model model
            ,@RequestParam("user_name") String userName
            ,@RequestParam("coin_name") String coinName)
    {
        BidTradeSetting bid_settings = tradeSettingService.getBidSetting(userName, coinName);
        AskTradeSetting ask_settings = tradeSettingService.getAskSetting(userName, coinName);
        model.addAttribute("user_name", userName);
        model.addAttribute("coin_name", coinName);
        model.addAttribute("bid_settings", bid_settings);
        model.addAttribute("ask_settings", ask_settings);

        return "settings/setting.html";
    }

    @PostMapping(value="/setting/save")
    public String save(TradeSettingForm form, RedirectAttributes redirectAttributes)
    {
        String userName = form.getUserName();
        String coinName = form.getCoinName();
        BidTradeSetting bid_settings = tradeSettingService.getBidSetting(userName, coinName);
        AskTradeSetting ask_settings = tradeSettingService.getAskSetting(userName, coinName);
        bid_settings.setCandle(BidTradeSetting.Candle.parse(form.getBidCandle()));
        ask_settings.setCandle(AskTradeSetting.Candle.parse(form.getAskCandle()));

        bid_settings.setReference(BidTradeSetting.Reference.valueOf(form.getBidReference()));
        ask_settings.setReference(AskTradeSetting.Reference.valueOf(form.getAskReference()));

        bid_settings.setConditionRatio(form.getBidConditionRatio());
        ask_settings.setConditionRatio(form.getAskConditionRatio());

        bid_settings.setPrice(form.getBidPrice());
        ask_settings.setPrice(form.getAskPrice());

        String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if(bid_settings.getPrice() > 0 && ask_settings.getPrice() > 0) {
            Coin coin = coinService.getCoinInfo(userName, password, coinName);
            coin.setIsRun(Coin.AutoRun.STOP);
        }
        int ret = tradeSettingService.save(bid_settings, ask_settings);
        if(ret == 0)
        {
            redirectAttributes.addFlashAttribute("isSaveOk", true);
        }

        String redirectURL = "redirect:/settings/edit?"
                + "user_name=" + bid_settings.getUserName() + "&"
                + "coin_name=" + bid_settings.getCoinName();
        return redirectURL;
    }

}
