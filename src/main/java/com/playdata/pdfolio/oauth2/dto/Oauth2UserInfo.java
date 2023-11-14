package com.playdata.pdfolio.oauth2.dto;

import com.playdata.pdfolio.global.exception.BadRequestException;
import com.playdata.pdfolio.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum Oauth2UserInfo {
    KAKAO("kakao"){
        @Override
        public Oauth2UserInfo extract(Map<String, Object> attributes) {
            Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");
            super.providerId = String.valueOf(attributes.get("id"));
            return this;
        }
    },

    GITHUB("github"){
        @Override
        public Oauth2UserInfo extract(Map<String, Object> attributes) {
            super.providerId = String.valueOf(attributes.get("id"));
            return this;
        }
    };

    private final String provider;
    protected String providerId;
    public static Oauth2UserInfo of(String provider, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(oauth2UserInfo -> oauth2UserInfo.provider.equals(provider))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_SUPPORTED_OAUTH2))
                .extract(attributes);
    }

    public abstract Oauth2UserInfo extract(Map<String, Object> attributes);
}
