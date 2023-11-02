package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.type.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void signup(){
        // given
        Member member = Member.builder().skills(new ArrayList<>()).build();

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
        assertThat(member.getSkills()).hasSize(2)
                .extracting("skillType")
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

    @DisplayName("회원정보를 수정한다.")
    @Test
    void update(){
        // given
        ArrayList<MemberSkill> skills = new ArrayList<>();
        MemberSkill php = MemberSkill.builder().skillType(SkillType.PHP).build();
        skills.add(php);

        Member member = Member.builder()
                .nickname("before name")
                .imageUrl("before image")
                .status(MemberStatus.MEMBER)
                .skills(skills)
                .build();

        // when
        member.update(
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
        assertThat(member.getSkills()).hasSize(2)
                .extracting("skillType")
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

    @DisplayName("회원탈퇴를 한다.")
    @Test
    void withdraw(){
        // given
        Member member = Member.builder().build();

        // when
        member.withdraw();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.WITHDRAWAL);
        assertThat(member.isDeleted()).isTrue();
    }
}