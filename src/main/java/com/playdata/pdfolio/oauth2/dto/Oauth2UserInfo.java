package com.playdata.pdfolio.oauth2.dto;

import com.playdata.pdfolio.oauth2.exception.NotSupportedOauth2Exception;
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
            super.userName = (String) properties.get("nickname");
            return this;
        }
    },

    GITHUB("github"){
        @Override
        public Oauth2UserInfo extract(Map<String, Object> attributes) {
            super.providerId = String.valueOf(attributes.get("id"));
            super.userName = (String) attributes.get("login");
            return this;
        }
    };

    private final String providerName;
    protected String providerId;
    protected String userName;
    protected String imageUrl;

    public static Oauth2UserInfo of(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider->provider.providerName.equals(providerName))
                .findFirst()
                .orElseThrow(NotSupportedOauth2Exception::new)
                .extract(attributes);
    }

    public abstract Oauth2UserInfo extract(Map<String, Object> attributes);
}
