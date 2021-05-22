package movingjin.tradingbot.login.service;

import lombok.AllArgsConstructor;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.login.domain.Role;
import movingjin.tradingbot.login.repository.MemberRepository;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.repository.AskTradeSettingJpaInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingJpaInterface;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final BidTradeSettingJpaInterface bidRepository;
    private final AskTradeSettingJpaInterface askRepository;
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        /*Use this system first time*/
        if(bidRepository.existsByUserName(userId) == false)
        {
            defaultSettingSave(userId);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        return new User(userId, "noPassword", authorities);
    }

    public int defaultSettingSave(String userName)
    {
        for(Coin.Token token: Coin.Token.values())
        {
            BidTradeSetting bidTradeSetting = new BidTradeSetting();
            AskTradeSetting askTradeSetting = new AskTradeSetting();

            bidTradeSetting.setUserName(userName);
            askTradeSetting.setUserName(userName);

            bidTradeSetting.setCoinName(token.name());
            askTradeSetting.setCoinName(token.name());

            bidTradeSetting.setCandle(BidTradeSetting.Candle.MINUTE_5);
            askTradeSetting.setCandle(AskTradeSetting.Candle.MINUTE_5);

            bidTradeSetting.setReference(BidTradeSetting.Reference.MA5);
            askTradeSetting.setReference(AskTradeSetting.Reference.MA5);

            bidTradeSetting.setConditionRatio(0.0);
            askTradeSetting.setConditionRatio(0.0);

            bidTradeSetting.setPrice(-1.0);
            askTradeSetting.setPrice(-1.0);

            bidRepository.save(bidTradeSetting);
            askRepository.save(askTradeSetting);
        }

        return 0;
    }
}
