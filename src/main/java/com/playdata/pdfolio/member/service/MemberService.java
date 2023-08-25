package com.playdata.pdfolio.member.service;

import com.playdata.pdfolio.domain.entity.common.Skill;
import com.playdata.pdfolio.domain.entity.member.Member;
import com.playdata.pdfolio.domain.entity.member.MemberSkill;
import com.playdata.pdfolio.domain.request.member.UpdateRequest;
import com.playdata.pdfolio.jwt.repository.LoginTokenRepository;
import com.playdata.pdfolio.member.exception.MemberNotFoundException;
import com.playdata.pdfolio.member.repository.MemberRepository;
import com.playdata.pdfolio.member.repository.MemberSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final LoginTokenRepository loginTokenRepository;

    public Member findByIdFetchMemberSkill(Long id){
        return memberRepository
                .findByIdFetchMemberSkill(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member findById(Long id){
        return memberRepository
                .findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public void updateBasic(Long id, UpdateRequest updateRequest){
        Member member = findById(id);

        member.update(
                updateRequest.nickName(),
                updateRequest.imageUrl()
        );
    }

    public void updateContainSkills(Long id, UpdateRequest updateRequest) {
        updateBasic(id, updateRequest);
        changeMemberSkill(id, updateRequest.skills());
    }

    private void changeMemberSkill(Long id, List<String> skills){
        Member member = findById(id);
        memberSkillRepository.deleteByMember(member);

        List<MemberSkill> newSkills = skills
                .stream()
                .map(Skill::valueOf)
                .map(skill -> MemberSkill.builder()
                        .member(member)
                        .skill(skill)
                        .build())
                .collect(Collectors.toList());

        memberSkillRepository.saveAll(newSkills);
    }

    public void withdraw(Long memberId) {
        Member member = findById(memberId);

        loginTokenRepository.deleteByMember(member);
        memberSkillRepository.deleteByMember(member);
        memberRepository.delete(member);
    }
}
