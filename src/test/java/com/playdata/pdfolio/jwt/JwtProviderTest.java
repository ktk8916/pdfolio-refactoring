package com.playdata.pdfolio.jwt;

import com.playdata.pdfolio.member.domain.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private JwtProvider jwtProvider = new JwtProvider();
    private String SECRET_KEY = "테스트를하기위해서사용하는의미없는문자열인데이정도면됐지않을까";

    @BeforeEach
    public void setup() {
        // 스프링의 @Value 로 대입되는 값을
        // 단위테스트로 사용하기 위함
        ReflectionTestUtils.setField(jwtProvider, "SECRET_KEY", SECRET_KEY);
    }

    @DisplayName("member객체로 토큰을 생성한다.")
    @Test
    void generateToken(){
        // given
        Member member = Member.builder()
                .nickname("철수")
                .imageUrl("www.cdn.com")
                .build();

        // when
        String token = jwtProvider.generateToken(member);

        // then
        Claims claims = (Claims) Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parse(token)
                .getBody();

        String nickname = claims.get("nickname", String.class);
        String imageUrl = claims.get("imageUrl", String.class);

        assertThat(nickname).isEqualTo("철수");
        assertThat(imageUrl).isEqualTo("www.cdn.com");
    }

    @DisplayName("토큰에서 tokenInfo 객체를 추출해낸다.")
    @Test
    void extractUser(){
        // given
        Member member = Member.builder()
                .nickname("철수")
                .imageUrl("www.cdn.com")
                .build();

        String token = jwtProvider.generateToken(member);

        // when
        TokenInfo tokenInfo = jwtProvider.extractUser(token);

        // then
        assertThat(tokenInfo.getNickname()).isEqualTo("철수");
        assertThat(tokenInfo.getImageUrl()).isEqualTo("www.cdn.com");
    }

    @DisplayName("토큰이 유효한 지 검사한다. 유효한 토큰이면 true를 반환한다.")
    @Test
    void isValidToken(){
        // given
        Member member = Member.builder()
                .nickname("철수")
                .imageUrl("www.cdn.com")
                .build();

        String token = jwtProvider.generateToken(member);

        // when
        boolean validate = jwtProvider.isValidToken(token);

        // then
        assertThat(validate).isTrue();
    }

    @DisplayName("토큰이 유효한 지 검사한다. 유효하지 않은 토큰이면 false를 반환한다.")
    @Test
    void invalidToken(){
        // given
        String token = "aaaaaaaa.aaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaa";

        // when
        boolean validate = jwtProvider.isValidToken(token);

        // then
        assertThat(validate).isFalse();
    }
}