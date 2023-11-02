package com.playdata.pdfolio.member.service;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;
import com.playdata.pdfolio.member.domain.entity.MemberStatus;
import com.playdata.pdfolio.member.domain.request.SignupRequest;
import com.playdata.pdfolio.member.domain.request.UpdateRequest;
import com.playdata.pdfolio.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager entityManager;

    @DisplayName("회원가입을 한다.")
    @Test
    void signup(){
        // given
        Member member = memberRepository.save(Member.builder().build());
        SignupRequest signupRequest = new SignupRequest("nick", "www.cdn.com", List.of("java", "spring"));

        clearContext();

        // when
        memberService.signup(member.getId(), signupRequest);

        // then
        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getNickname()).isEqualTo("nick");
        assertThat(findMember.getImageUrl()).isEqualTo("www.cdn.com");
        assertThat(findMember.getStatus()).isEqualTo(MemberStatus.MEMBER);
        assertThat(findMember.getSkills()).hasSize(2)
                .extracting("skillType")
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

    @DisplayName("회원정보를 수정한다.")
    @Test
    void editProfile(){
        // given
        Member member = Member.builder()
                .nickname("before")
                .imageUrl("www.before.com")
                .skills(List.of(MemberSkill.builder().skillType(SkillType.PHP).build()))
                .build();
        Member savedMember = memberRepository.save(member);
        UpdateRequest updateRequest = new UpdateRequest("nick", "www.cdn.com", List.of("java", "spring"));

        clearContext();

        // when
        memberService.editProfile(savedMember.getId(), updateRequest);

        // then
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getNickname()).isEqualTo("nick");
        assertThat(findMember.getImageUrl()).isEqualTo("www.cdn.com");
        assertThat(findMember.getSkills())
                .extracting("skillType").hasSize(2)
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

    private void clearContext() {
        entityManager.flush();
        entityManager.clear();
    }

}