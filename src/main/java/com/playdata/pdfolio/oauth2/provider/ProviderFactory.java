package com.playdata.pdfolio.oauth2.provider;

import com.playdata.pdfolio.oauth2.exception.NotSupportedOauth2Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProviderFactory {

    private final GithubProvider githubProvider;
    private final KakaoProvider kaKaoProvider;

    public Oauth2Provider getProvider(String providerName){

        return switch (providerName) {
            case "github" -> githubProvider;
            case "kakao" -> kaKaoProvider;
            default -> throw new NotSupportedOauth2Exception();
        };
    }
}
