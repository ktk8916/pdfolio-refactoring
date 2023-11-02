package com.playdata.pdfolio.auth.service;

import com.playdata.pdfolio.jwt.JwtProvider;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberStatus;
import com.playdata.pdfolio.member.domain.response.AuthResponse;
import com.playdata.pdfolio.member.repository.MemberRepository;
import com.playdata.pdfolio.oauth2.client.Oauth2Client;
import com.playdata.pdfolio.oauth2.dto.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final Oauth2Client oauth2Client;
    private final JwtProvider jwtProvider;

    public AuthResponse authenticate(String provider, String code) {
        Oauth2UserInfo oauth2UserInfo = oauth2Client.authenticate(provider, code);

        Member member = memberRepository
                .findByProviderAndProviderId(oauth2UserInfo.getProvider(), oauth2UserInfo.getProviderId())
                .orElseGet(() -> memberRepository.save(
                        Member.fromOauth2(
                                oauth2UserInfo.getProvider(),
                                oauth2UserInfo.getProviderId())));

        String token = jwtProvider.generateToken(member);

        return AuthResponse.of(getRedirect(member), token);
    }

    private String getRedirect(Member member){
        return member.getStatus() == MemberStatus.MEMBER ? "/" : "/signup";
    }
}
