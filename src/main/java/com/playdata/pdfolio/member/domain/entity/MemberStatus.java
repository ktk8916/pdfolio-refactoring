package com.playdata.pdfolio.member.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberStatus {
    UNAFFILIATED("미가입"),
    MEMBER("회원"),
    WITHDRAWAL("탈퇴")

    ;

    private final String text;

}
