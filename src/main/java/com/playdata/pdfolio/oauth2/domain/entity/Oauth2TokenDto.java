package com.playdata.pdfolio.oauth2.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Oauth2TokenDto(
        @JsonProperty("access_token") String token,
        @JsonProperty("token_type") String type,
        String scope) {
}
