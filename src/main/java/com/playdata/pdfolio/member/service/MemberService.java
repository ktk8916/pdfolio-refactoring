package com.playdata.pdfolio.member.service;

import com.playdata.pdfolio.global.exception.ErrorCode;
import com.playdata.pdfolio.global.exception.NotFoundException;
import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;
import com.playdata.pdfolio.member.domain.request.SignupRequest;
import com.playdata.pdfolio.member.domain.request.UpdateRequest;
import com.playdata.pdfolio.member.domain.response.MemberDetailResponse;
import com.playdata.pdfolio.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDetailResponse getMyProfile(Long id){
        return MemberDetailResponse.fromEntity(findByIdFetchSkill(id));
    }

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
                updateRequest.nickname(),
                updateRequest.imageUrl(),
                SkillType.convertList(updateRequest.skills()).stream()
                        .map(skillType -> MemberSkill.of(member, skillType))
                        .toList()
        );
    }

    public Member findByIdFetchSkill(Long id){
        return memberRepository
                .findByIdFetchSkill(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CONTENT, Member.class, id));
    }

    private Member findById(Long id){
        return memberRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CONTENT, Member.class, id));
    }

    public void withdraw(Long memberId) {
        Member member = findById(memberId);
        member.withdraw();
    }
}
