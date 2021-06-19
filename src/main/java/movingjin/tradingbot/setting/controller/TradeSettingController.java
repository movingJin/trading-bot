package movingjin.tradingbot.setting.controller;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.dto.AskTradeSettingDTO;
import movingjin.tradingbot.setting.dto.BidTradeSettingDTO;
import movingjin.tradingbot.setting.service.TradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
            ,@RequestParam("coin_name") String coinName)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        }
        BidTradeSetting bid_settings = tradeSettingService.getBidSetting(userName, coinName);
        Double currentPrice = coinService.getCurrentPrice(coinName);
        BidTradeSettingDTO bidDTO = new BidTradeSettingDTO();
        bidDTO.setReference(bid_settings.getReference().name());
        bidDTO.setConditionRatio(bid_settings.getConditionRatio());
        bidDTO.setQuantity(bid_settings.getQuantity());
        bidDTO.setPrice(bid_settings.getQuantity() * currentPrice);

        AskTradeSetting ask_settings = tradeSettingService.getAskSetting(userName, coinName);
        AskTradeSettingDTO askDTO = new AskTradeSettingDTO();
        askDTO.setReference(ask_settings.getReference().name());
        askDTO.setConditionRatio(ask_settings.getConditionRatio());

        model.addAttribute("user_name", userName);
        model.addAttribute("coin_name", coinName);
        model.addAttribute("bid_settings", bidDTO);
        model.addAttribute("ask_settings", askDTO);
        model.addAttribute("currentPrice", currentPrice);

        return "settings/setting.html";
    }

    @PostMapping(value="/setting/save")
    public String save(TradeSettingForm form, RedirectAttributes redirectAttributes)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        String password = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
            password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        }
        String coinName = form.getCoinName();
        BidTradeSetting bid_settings = tradeSettingService.getBidSetting(userName, coinName);
        AskTradeSetting ask_settings = tradeSettingService.getAskSetting(userName, coinName);

        bid_settings.setReference(BidTradeSetting.Reference.valueOf(form.getBidReference()));
        ask_settings.setReference(AskTradeSetting.Reference.valueOf(form.getAskReference()));

        bid_settings.setConditionRatio(form.getBidConditionRatio());
        ask_settings.setConditionRatio(form.getAskConditionRatio());

        bid_settings.setQuantity(form.getBidQuantity());


        if(bid_settings.getQuantity() > 0) {
            Coin coin = coinService.getCoinInfo(userName, password, coinName);
            coin.setIsRun(Coin.AutoRun.STOP);
        }
        int ret = tradeSettingService.save(bid_settings, ask_settings);
        if(ret == 0)
        {
            redirectAttributes.addFlashAttribute("isSaveOk", true);
        }

        String redirectURL = "redirect:/settings/edit?"
                + "coin_name=" + bid_settings.getCoinName();
        return redirectURL;
    }

}
