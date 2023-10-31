package com.playdata.pdfolio.oauth2.domain.dto;

import lombok.Builder;

@Builder
public record TokenDto(
        String grantType,
        String accessToken,
        String refreshToken) {
}
