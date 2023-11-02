package com.playdata.pdfolio.member.controller;

import com.playdata.pdfolio.jwt.TokenInfo;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.request.SignupRequest;
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

    @PutMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@AuthenticationPrincipal TokenInfo tokenInfo, @RequestBody SignupRequest signupRequest){
        memberService.signup(tokenInfo.getId(), signupRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void editProfile(
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody UpdateRequest updateRequest){
        memberService.editProfile(tokenInfo.getId(), updateRequest);
    }

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
}
