package com.playdata.pdfolio.domain.response.member;

import com.playdata.pdfolio.domain.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponse {

    private Long memberId;
    private String nickName;
    private String providerId;
    private String providerName;
    private String image;

    public MemberInfoResponse(
            final Long memberId,
            final String nickName,
            final String providerId,
            final String providerName,
            final String image) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.image = image;
    }

    public static MemberInfoResponse of(final Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getNickName(),
                member.getProviderId(),
                member.getProviderName(),
                member.getImageUrl()
        );
    }
}
