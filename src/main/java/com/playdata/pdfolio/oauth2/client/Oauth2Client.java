package com.playdata.pdfolio.oauth2.client;

import com.playdata.pdfolio.oauth2.dto.Oauth2AccessToken;
import com.playdata.pdfolio.oauth2.dto.Oauth2UserInfo;
import com.playdata.pdfolio.oauth2.provider.Oauth2Provider;
import com.playdata.pdfolio.oauth2.provider.ProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Oauth2Client {

    private final ProviderFactory providerFactory;

    public Oauth2UserInfo authenticate(String provider, String code) {
        Oauth2AccessToken accessToken = getAccessToken(provider, code);
        return getUserInfo(provider, accessToken.token());
    }

    private Oauth2AccessToken getAccessToken(String provider, String code) {
        Oauth2Provider oauth2provider = providerFactory.getProvider(provider);

        return WebClient.create()
                .post()
                .uri(oauth2provider.getAccessTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, oauth2provider))
                .retrieve()
                .bodyToMono(Oauth2AccessToken.class)
                .block();
    }

    private Oauth2UserInfo getUserInfo(String provider, String accessToken) {
        Oauth2Provider oauth2provider = providerFactory.getProvider(provider);

        Map<String, Object> attributes = WebClient.create()
                .get()
                .uri(oauth2provider.getUserInfoUri())
                .headers(header -> header.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return Oauth2UserInfo.of(oauth2provider.getName(), attributes);
    }

    private MultiValueMap<String, String> tokenRequest(String code, Oauth2Provider oauth2provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", oauth2provider.getClientId());
        formData.add("client_secret", oauth2provider.getClientSecret());
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oauth2provider.getRedirectUri());
        return formData;
    }
}
