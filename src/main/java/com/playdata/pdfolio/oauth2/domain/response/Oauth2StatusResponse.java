package com.playdata.pdfolio.oauth2.domain.response;

public record Oauth2StatusResponse(
        Boolean isNewMember,
        String providerName,
        String accessToken) {
}
