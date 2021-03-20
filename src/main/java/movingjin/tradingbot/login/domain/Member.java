package movingjin.tradingbot.login.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String connectKey;

    private String secretKey;

    private String accountId;

    private double balance;

    @Builder
    public Member(String connectKey, String accountId, String secretKey, double balance) {
        this.connectKey = connectKey;
        this.secretKey = secretKey;
        this.accountId = accountId;
        this.balance = balance;
    }
}
