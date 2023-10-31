package com.playdata.pdfolio.member.domain.response;

import com.playdata.pdfolio.oauth2.domain.dto.TokenDto;

public record LoginResponse(TokenDto jwtTokenDto) {
}
