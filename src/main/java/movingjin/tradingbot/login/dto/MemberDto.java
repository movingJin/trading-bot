package movingjin.tradingbot.login.dto;


import lombok.*;
import movingjin.tradingbot.login.domain.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
    private String connectKey;
    private String accountId;
    private String secretKey;
    private LocalDateTime createdDate;

    public Member toEntity(){
        return Member.builder()
                .connectKey(connectKey)
                .accountId(accountId)
                .secretKey(secretKey)
                .build();
    }

    @Builder
    public MemberDto(String connectKey, String accountId, String secretKey) {
        this.connectKey = connectKey;
        this.accountId = accountId;
        this.secretKey = secretKey;
    }
}