package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.type.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("회원가입 메서드로 멤버의 값을 변경한다.")
    @Test
    void signup(){
        // given
        Member member = Member.fromId(1L);

        // when
        member.signup(
                "nick",
                "www.cdn.com",
                List.of(MemberSkill.builder().skillType(SkillType.JAVA).build(),
                        MemberSkill.builder().skillType(SkillType.SPRING).build()
                )
        );

        // then
        assertThat(member.getNickname()).isEqualTo("nick");
        assertThat(member.getImageUrl()).isEqualTo("www.cdn.com");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.MEMBER);
        assertThat(member.getSkills())
                .extracting("skillType")
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

}