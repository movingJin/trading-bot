package movingjin.tradingbot.setting.controller;

import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TradeSettingController {
    private TradeSettingService tradeSettingService;

    @Autowired
    public void TradeSettingController(TradeSettingService tradeSettingService)
    {
        this.tradeSettingService = tradeSettingService;

    }

    @GetMapping("/settings/edit")
    public String editSetting(Model model, @RequestParam("coin_name") String coinName)
    {
        BidTradeSetting bid_settings = tradeSettingService.getBidSetting(coinName).get();
        AskTradeSetting ask_settings = tradeSettingService.getAskSetting(coinName).get();
        model.addAttribute("coin_name", coinName);
        model.addAttribute("bid_settings", bid_settings);
        model.addAttribute("ask_settings", ask_settings);

        return "settings/setting.html";
    }

    @PostMapping(value="/setting/save")
    public String save(TradeSettingForm form, RedirectAttributes redirectAttributes)
    {
        String coin_name = form.getCoinName();
        BidTradeSetting bid_settings = tradeSettingService.getBidSetting(coin_name).get();
        AskTradeSetting ask_settings = tradeSettingService.getAskSetting(coin_name).get();
        bid_settings.setCandle(BidTradeSetting.Candle.parse(form.getBidCandle()));
        ask_settings.setCandle(AskTradeSetting.Candle.parse(form.getAskCandle()));

        bid_settings.setReference(BidTradeSetting.Reference.valueOf(form.getBidReference()));
        ask_settings.setReference(AskTradeSetting.Reference.valueOf(form.getAskReference()));

        bid_settings.setCondition_ratio(form.getBidCondition_ratio());
        ask_settings.setCondition_ratio(form.getAskCondition_ratio());

        bid_settings.setPrice(form.getBidPrice());
        ask_settings.setPrice(form.getAskPrice());

        int ret = tradeSettingService.save(bid_settings, ask_settings);
        if(ret == 0)
        {
            redirectAttributes.addFlashAttribute("isSaveOk", true);
        }

        String redirectURL = "redirect:/settings/edit?coin_name=" + bid_settings.getCoinName();
        return redirectURL;
    }

}
