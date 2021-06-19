package movingjin.tradingbot.login.service;

import movingjin.tradingbot.bithumApi.Api_Client;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_POST;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InternalAuthenticationServiceException("Authentication is null");
        }
        String userId = authentication.getName();
        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }
        String password = authentication.getCredentials().toString();
        /*Validate API keys*/
        if(!isApiKeyValidate(userId, password))
        {
            throw new BadCredentialsException("API kes is not validate");
        }


        UserDetails loadedUser = memberService.loadUserByUsername(userId);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        } /* checker */
        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        if (!loadedUser.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }

         /* checker */
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        } /* 인증 완료 */
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, authentication.getCredentials(), loadedUser.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication); //return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean isApiKeyValidate(String connectKey, String secretKey)
    {
        boolean ret = false;
        Api_Client api = new Api_Client(connectKey, secretKey);

        HashMap<String, String> rgParams = new HashMap<String, String>();
        rgParams.put("order_currency", "BTC");
        rgParams.put("payment_currency", "KRW");

        try {
            String result = api.callApi("/info/account", rgParams, METHOD_POST);
            System.out.println(result);
            JSONObject jObject = new JSONObject(result);
            String status = jObject.getString("status");
            if(status.equals("0000"))
            {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
