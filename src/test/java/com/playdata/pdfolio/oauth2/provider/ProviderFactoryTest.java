package com.playdata.pdfolio.oauth2.provider;

import com.playdata.pdfolio.global.exception.BadRequestException;
import com.playdata.pdfolio.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ProviderFactoryTest {

    @Autowired
    private ProviderFactory providerFactory;

    private static Stream<Arguments> typeOfProviderName(){
        return Stream.of(
                Arguments.of("kakao", KakaoProvider.class),
                Arguments.of("github", GithubProvider.class)
        );
    }

    @DisplayName("provider이름으로 Oauth2Provider객체를 가져온다.")
    @MethodSource("typeOfProviderName")
    @ParameterizedTest
    void getProvider(String provider, Class<? extends Oauth2Provider> oauth2Provider){
        // given

        // when
        Oauth2Provider findProvider = providerFactory.getProvider(provider);

        // then
        assertThat(findProvider).isInstanceOf(oauth2Provider);
    }

    @DisplayName("유효하지 않은 이름일 경우 예외가 발생한다.")
    @Test
    void InvalidProvider(){
        // given
        String wrongName = "aaaaaaa";

        // when, when
        assertThatThrownBy(()->providerFactory.getProvider(wrongName))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.NOT_SUPPORTED_OAUTH2.name());
    }
}