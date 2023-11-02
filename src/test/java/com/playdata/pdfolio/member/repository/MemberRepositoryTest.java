package com.playdata.pdfolio.member.repository;

import com.playdata.pdfolio.member.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("oauth2 정보로 회원을 조회한다.")
    @Test
    void findByProviderAndProviderId(){
        // given
        String provider = "provider";
        String providerId = "kakao1234";

        Member member = Member.builder()
                .provider(provider)
                .providerId(providerId)
                .build();

        memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findByProviderAndProviderId(provider, providerId);

        // then
        assertThat(findMember).isPresent();
    }

    @DisplayName("oauth2 정보로 가입하지 않은 회원을 조회한다.")
    @Test
    void findByNotSignupMember(){
        // given
        String provider = "provider";
        String providerId = "kakao1234";

        // when
        Optional<Member> findMember = memberRepository.findByProviderAndProviderId(provider, providerId);

        // then
        assertThat(findMember).isEmpty();
    }



}