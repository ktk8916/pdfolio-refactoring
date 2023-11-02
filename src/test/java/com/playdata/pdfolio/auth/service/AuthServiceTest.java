package com.playdata.pdfolio.auth.service;

import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberStatus;
import com.playdata.pdfolio.member.domain.response.AuthResponse;
import com.playdata.pdfolio.member.repository.MemberRepository;
import com.playdata.pdfolio.oauth2.client.Oauth2Client;
import com.playdata.pdfolio.oauth2.dto.Oauth2UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @MockBean
    private Oauth2Client oauth2Client;

    @DisplayName("회원가입을 완료한 회원인 경우 redirect가 / 이고, access token이 발급된다.")
    @Test
    void authenticate(){
        // given
        String provider = "github";
        String providerId = "githubId";

        Member member = Member.builder()
                .provider(provider)
                .providerId(providerId)
                .status(MemberStatus.MEMBER)
                .build();

        memberRepository.save(member);

        Map<String, Object> attributes = Map.of("id", providerId);
        Oauth2UserInfo oauth2UserInfo = Oauth2UserInfo.of(provider, attributes);

        when(oauth2Client.authenticate(anyString(), anyString()))
                .thenReturn(oauth2UserInfo);

        // when
        AuthResponse authResponse = authService.authenticate(provider, "authorization");

        // then
        assertThat(authResponse.redirect()).isEqualTo("/");
        assertThat(authResponse.token()).isNotNull();
    }

    @DisplayName("회원가입을 완료하지 않은 회원인 경우 redirect가 /signup 이고, access token이 발급된다.")
    @Test
    void unaffiliatedAuthenticate(){
        // given
        String provider = "github";
        String providerId = "githubId";

        Member member = Member.builder()
                .provider(provider)
                .providerId(providerId)
                .status(MemberStatus.UNAFFILIATED)
                .build();

        memberRepository.save(member);

        Map<String, Object> attributes = Map.of("id", providerId);
        Oauth2UserInfo oauth2UserInfo = Oauth2UserInfo.of(provider, attributes);

        when(oauth2Client.authenticate(anyString(), anyString()))
                .thenReturn(oauth2UserInfo);

        // when
        AuthResponse authResponse = authService.authenticate(provider, "authorization");

        // then
        assertThat(authResponse.redirect()).isEqualTo("/signup");
        assertThat(authResponse.token()).isNotNull();
    }

    @DisplayName("신규 회원인 경우 redirect가 /signup 이고, 회원가입용 access token이 발급된다.")
    @Test
    void newMemberAuthenticate(){
        // given
        String provider = "github";
        String providerId = "githubId";

        Map<String, Object> attributes = Map.of("id", providerId);
        Oauth2UserInfo oauth2UserInfo = Oauth2UserInfo.of(provider, attributes);

        when(oauth2Client.authenticate(anyString(), anyString()))
                .thenReturn(oauth2UserInfo);

        // when
        AuthResponse authResponse = authService.authenticate(provider, "authorization");

        // then
        assertThat(authResponse.redirect()).isEqualTo("/signup");
        assertThat(authResponse.token()).isNotNull();
    }

}