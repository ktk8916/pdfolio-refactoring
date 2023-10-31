package com.playdata.pdfolio.member.controller;

import com.playdata.pdfolio.jwt.TokenInfo;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.request.UpdateRequest;
import com.playdata.pdfolio.member.domain.response.MemberResponse;
import com.playdata.pdfolio.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@AuthenticationPrincipal TokenInfo tokenInfo){
        memberService.withdraw(tokenInfo.getMemberId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse me(@AuthenticationPrincipal TokenInfo tokenInfo){
        Member member = memberService.findByIdFetchMemberSkill(tokenInfo.getMemberId());
        return MemberResponse.from(member);
    }
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateBasic(
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody UpdateRequest updateRequest){
        memberService.updateBasic(tokenInfo.getMemberId(), updateRequest);
    }

    @PutMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateContainSkills(
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody UpdateRequest updateRequest)
    {
        memberService.updateContainSkills(
                tokenInfo.getMemberId(),
                updateRequest);
    }

}
