package com.playdata.pdfolio.oauth2.service;

import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.repository.MemberRepository;
import com.playdata.pdfolio.oauth2.domain.entity.Oauth2TokenDto;
import com.playdata.pdfolio.oauth2.domain.entity.Oauth2UserInfo;
import com.playdata.pdfolio.oauth2.domain.response.Oauth2StatusResponse;
import com.playdata.pdfolio.oauth2.provider.Oauth2Provider;
import com.playdata.pdfolio.oauth2.provider.ProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class Oauth2Service {

    private final ProviderFactory providerFactory;
    private final MemberRepository memberRepository;

    public Oauth2StatusResponse authenticate(String providerName, String code) {
        Oauth2TokenDto accessToken = getAccessToken(providerName, code);
        Oauth2UserInfo userInfo = getUserInfo(providerName, accessToken.token());

        Optional<Member> member = memberRepository.findByProviderIdAndProviderName(
                userInfo.getProviderId(),
                userInfo.getProviderName()
        );

        return new Oauth2StatusResponse(
                member.isEmpty(),
                providerName,
                accessToken.token());
    }

    private Oauth2TokenDto getAccessToken(String providerName, String code) {
        Oauth2Provider provider = providerFactory.getProvider(providerName);
        return WebClient.create()
                .post()
                .uri(provider.getAccessTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(Oauth2TokenDto.class)
                .block();
    }

    private Oauth2UserInfo getUserInfo(String providerName, String accessToken) {
        Oauth2Provider provider = providerFactory.getProvider(providerName);
        Map<String, Object> attributes = WebClient.create()
                .get()
                .uri(provider.getUserInfoUri())
                .headers(header -> header.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        return Oauth2UserInfo.of(provider.getName(), attributes);
    }

    private MultiValueMap<String, String> tokenRequest(String code, Oauth2Provider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", provider.getClientId());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        return formData;
    }
}
