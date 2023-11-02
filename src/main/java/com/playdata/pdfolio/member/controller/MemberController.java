package com.playdata.pdfolio.member.controller;

import com.playdata.pdfolio.jwt.TokenInfo;
import com.playdata.pdfolio.member.domain.request.SignupRequest;
import com.playdata.pdfolio.member.domain.request.UpdateRequest;
import com.playdata.pdfolio.member.domain.response.MemberDetailResponse;
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

    @GetMapping("/me")
    public MemberDetailResponse getMyProfile(@AuthenticationPrincipal TokenInfo tokenInfo){
        return memberService.getMyProfile(tokenInfo.getId());
    }

    @PutMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@AuthenticationPrincipal TokenInfo tokenInfo, @RequestBody SignupRequest signupRequest){
        memberService.signup(tokenInfo.getId(), signupRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void editProfile(@AuthenticationPrincipal TokenInfo tokenInfo, @RequestBody UpdateRequest updateRequest){
        memberService.editProfile(tokenInfo.getId(), updateRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@AuthenticationPrincipal TokenInfo tokenInfo){
        memberService.withdraw(tokenInfo.getId());
    }
}
