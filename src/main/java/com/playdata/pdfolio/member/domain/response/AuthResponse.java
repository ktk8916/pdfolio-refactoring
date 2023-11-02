package com.playdata.pdfolio.member.domain.response;

public record AuthResponse(String redirect, String token) {

    public static AuthResponse of(String redirect, String token){
        return new AuthResponse(redirect, token);
    }
}
