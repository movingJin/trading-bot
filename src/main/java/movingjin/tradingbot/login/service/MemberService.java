package movingjin.tradingbot.login.service;

import lombok.AllArgsConstructor;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.login.domain.Role;
import movingjin.tradingbot.login.dto.MemberDto;
import movingjin.tradingbot.login.repository.MemberRepository;
import movingjin.tradingbot.setting.domain.AskTradeSetting;
import movingjin.tradingbot.setting.domain.BidTradeSetting;
import movingjin.tradingbot.setting.repository.AskTradeSettingInterface;
import movingjin.tradingbot.setting.repository.BidTradeSettingInterface;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final BidTradeSettingInterface bidRepository;
    private final AskTradeSettingInterface askRepository;
    private MemberRepository memberRepository;

    @Transactional
    public Long joinUser(MemberDto memberDto) {
/*
        // 비밀번호 암호화
        if(isApiKeyValidate(memberDto.getConnectKey(),memberDto.getSecretKey()))
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            memberDto.setSecretKey(passwordEncoder.encode(memberDto.getSecretKey()));
            BidTradeSetting bidTradeSetting = new BidTradeSetting();
            AskTradeSetting askTradeSetting = new AskTradeSetting();
            defaultSettingSave(bidTradeSetting, askTradeSetting);
        }
        //return memberRepository.save(memberDto.toEntity()).getId();

 */
        return 0L;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        /*
        Optional<Member> userEntityWrapper = memberRepository.findByAccountId(accountId);
        Member userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin@example.com").equals(accountId)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(userEntity.getAccountId(), userEntity.getSecretKey(), authorities);

         */

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
