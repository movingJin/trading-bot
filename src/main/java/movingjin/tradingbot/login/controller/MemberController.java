package movingjin.tradingbot.login.controller;

import movingjin.tradingbot.login.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService)
    {
        this.memberService = memberService;
    }
/*
    // 메인 페이지
    @GetMapping("/temp")
    public String index() {
        return "/temp";
    }
*/
    // 회원가입 페이지
    @GetMapping("/user/signUp")
    public String dispSignup() {
        return "https://www.bithumb.com/api_support/management_api";
    }

    // 로그인 페이지
    @GetMapping("/user/signIn")
    public String dispLogin() {
        return "/user/signIn";
    }

    // 로그인 결과 페이지
    @GetMapping("/user/signIn/result")
    public String dispLoginResult() {
        return "redirect:/";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/user/signOut/result")
    public String dispLogout() {
        return "/user/signOut";
    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public String dispDenied() {
        return "/user/denied";
    }

    // 내 정보 페이지
    @GetMapping("/user/myinfo")
    public String dispMyInfo() {
        return "/user/myinfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "/user/admin";
    }
}
