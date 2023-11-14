package com.playdata.pdfolio.oauth2.provider;

import com.playdata.pdfolio.global.exception.BadRequestException;
import com.playdata.pdfolio.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProviderFactory {

    private final GithubProvider githubProvider;
    private final KakaoProvider kaKaoProvider;

    public Oauth2Provider getProvider(String provider){
        return switch (provider) {
            case "github" -> githubProvider;
            case "kakao" -> kaKaoProvider;
            default -> throw new BadRequestException(ErrorCode.NOT_SUPPORTED_OAUTH2);
        };
    }
}
