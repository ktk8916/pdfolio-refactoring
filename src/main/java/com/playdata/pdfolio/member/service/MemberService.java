package com.playdata.pdfolio.member.service;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;
import com.playdata.pdfolio.member.domain.request.SignupRequest;
import com.playdata.pdfolio.member.domain.request.UpdateRequest;
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

    public void signup(Long id, SignupRequest signupRequest) {
        Member member = findById(id);

        member.signup(
                signupRequest.nickname(),
                signupRequest.imageUrl(),
                SkillType.convertList(signupRequest.skills()).stream()
                        .map(skillType -> MemberSkill.of(member, skillType))
                        .toList()
        );
    }

    public void editProfile(Long id, UpdateRequest updateRequest){
        Member member = findByIdFetchSkill(id);

        member.update(
                updateRequest.nickName(),
                updateRequest.imageUrl(),
                SkillType.convertList(updateRequest.skills()).stream()
                        .map(skillType -> MemberSkill.of(member, skillType))
                        .toList()
        );
    }

    public Member findByIdFetchSkill(Long id){
        return memberRepository
                .findByIdFetchSkill(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Member findById(Long id){
        return memberRepository
                .findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void changeMemberSkill(Long id, List<String> skills){
        Member member = findById(id);
        memberSkillRepository.deleteByMember(member);

        List<MemberSkill> newSkills = skills
                .stream()
                .map(SkillType::valueOf)
                .map(skill -> MemberSkill.builder()
                        .member(member)
                        .skillType(skill)
                        .build())
                .collect(Collectors.toList());

        memberSkillRepository.saveAll(newSkills);
    }

    public void withdraw(Long memberId) {
        Member member = findById(memberId);

        memberSkillRepository.deleteByMember(member);
        memberRepository.delete(member);
    }


}
