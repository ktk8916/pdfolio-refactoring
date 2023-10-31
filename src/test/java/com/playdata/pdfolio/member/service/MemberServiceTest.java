package com.playdata.pdfolio.member.service;

import com.playdata.pdfolio.global.type.Skill;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;
import com.playdata.pdfolio.member.domain.request.UpdateRequest;
import com.playdata.pdfolio.global.exception.ExceptionType;
import com.playdata.pdfolio.member.exception.MemberNotFoundException;
import com.playdata.pdfolio.member.repository.MemberRepository;
import com.playdata.pdfolio.member.repository.MemberSkillRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberSkillRepository memberSkillRepository;
    @Autowired
    EntityManager entityManager;

    Member member;
    @BeforeEach
    void init(){
        Member member = Member.builder()
                .id(1L)
                .name("testName")
                .nickName("testNickName")
                .imageUrl("testImage")
                .build();

        this.member = memberRepository.save(member);
    }

    @AfterEach
    void clear(){
        memberRepository.deleteAllInBatch();
    }

    @Nested
    class 멤버_id_조회 {
        @Test
        void 조회_성공() {
            //given
            Long id = member.getId();
            //when
            Member findMember = memberService.findById(id);
            //then
            assertThat(findMember.getId()).isEqualTo(member.getId());
            assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
            assertThat(findMember.getImageUrl()).isEqualTo(member.getImageUrl());
        }

        @Test
        void 조회_실패시_예외() {
            //given
            Long invalidId = 9999L;
            //when, then
            assertThrows(
                    MemberNotFoundException.class,
                    () -> memberService.findById(invalidId),
                    ExceptionType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @Nested
    class 멤버_정보_수정{

        String newNickName = "newNickName";
        String newImageUrl = "newImageUrl";
        List<String> newSkills = Arrays.asList(Skill.JAVASCRIPT.name(),
                Skill.TYPESCRIPT.name(),
                Skill.REACT.name());
        @BeforeEach
        void initSkills(){
            List<String> skills = Arrays.asList(Skill.JAVA.name(), Skill.SPRING.name());
            Set<MemberSkill> memberSkills = skills.stream()
                    .map(skill ->
                            MemberSkill.builder()
                                    .member(member)
                                    .skill(Skill.valueOf(skill))
                                    .build())

                    .collect(Collectors.toSet());
            memberSkillRepository.saveAll(memberSkills);
        }

        @AfterEach
        void clearSkills(){
            memberSkillRepository.deleteAllInBatch();
        }

        @Test
        void 기본_정보_수정_성공(){
            //given
            UpdateRequest updateRequest = new UpdateRequest(
                    newNickName,
                    newImageUrl,
                    new ArrayList<>());

            //when
            memberService.updateBasic(member.getId(), updateRequest);

            //then
            assertThat(member.getNickname()).isEqualTo(newNickName);
            assertThat(member.getImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        void 스킬_정보_포함_수정_성공(){
            //given
            UpdateRequest updateRequest = new UpdateRequest(
                    newNickName,
                    newImageUrl,
                    newSkills);

            //when
            memberService.updateContainSkills(member.getId(), updateRequest);

            entityManager.flush();
            entityManager.clear();


            List<String> skills = member.getSkills().stream()
                    .map(MemberSkill::getSkill)
                    .map(Skill::name)
                    .collect(Collectors.toList());

            //then
            assertThat(member.getNickname()).isEqualTo(newNickName);
            assertThat(member.getImageUrl()).isEqualTo(newImageUrl);
            assertThat(skills).containsAll(newSkills);
        }

        @Test
        void 스킬_정보_포함_수정시_멤버스킬테이블_반영(){
            //given

            UpdateRequest updateRequest = new UpdateRequest(
                    newNickName,
                    newImageUrl,
                    newSkills);

            //when
            memberService.updateContainSkills(member.getId(), updateRequest);

            //then
            List<MemberSkill> skills = memberSkillRepository.findByMember(member);
            assertThat(skills).hasSize(newSkills.size());
        }
    }

}