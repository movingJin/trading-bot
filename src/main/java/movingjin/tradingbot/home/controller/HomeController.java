package movingjin.tradingbot.home.controller;

import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.home.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {
    private final CoinService coinService;
    private List<Coin> coins;

    @Autowired
    public HomeController (CoinService coinService)
    {
        this.coinService = coinService;
        coins = coinService.getCoinInfo();
    }

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("coins", coins);
        return "home";
    }

    @RequestMapping(value="/run_or_stop", method= RequestMethod.POST)
    public String auto_run(Model model, @RequestParam("btn") String _idx)
    {
        int idx = Integer.parseInt(_idx);
        boolean isRun = coins.get(idx).getIsRun();
        coins.get(idx).setIsRun(!isRun);

        return "redirect:/";
    }
}
