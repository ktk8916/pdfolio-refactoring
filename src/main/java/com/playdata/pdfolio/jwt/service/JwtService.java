package com.playdata.pdfolio.jwt.service;

import com.playdata.pdfolio.auth.exception.AccessTokenExpiredException;
import com.playdata.pdfolio.auth.exception.InvalidTokenException;
import com.playdata.pdfolio.domain.dto.jwt.JwtTokenDto;
import com.playdata.pdfolio.domain.entity.jwt.LoginToken;
import com.playdata.pdfolio.domain.entity.member.Member;
import com.playdata.pdfolio.jwt.repository.LoginTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {

    private final LoginTokenRepository loginTokenRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final SignatureAlgorithm HS256 = SignatureAlgorithm.HS256;
    private final long ACCESS_TOKEN_VALID_TIME = 3 * 60 * 60 * 1000L; // 3시간
    private final long REFRESH_TOKEN_VALID_TIME = 3 * 24 * 60 * 60 * 1000L; // 3일

    public JwtTokenDto updateRefreshToken(String refreshToken){
        LoginToken loginToken = loginTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(InvalidTokenException::new);
        return generateToken(loginToken.getMember());
    }


    public JwtTokenDto generateToken(Member member){

        final SecretKeySpec KEY = new SecretKeySpec(SECRET_KEY.getBytes(), HS256.getJcaName());
        long now = System.currentTimeMillis();

        String accessToken = Jwts.builder()
                .claim("id", member.getId())
                .claim("nickName", member.getNickName())
                .setExpiration(new Date(now + ACCESS_TOKEN_VALID_TIME))
                .signWith(KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("id", member.getId())
                .setExpiration(new Date(now + REFRESH_TOKEN_VALID_TIME))
                .signWith(KEY)
                .compact();

        loginTokenRepository.save(
                LoginToken.builder()
                        .refreshToken(refreshToken)
                        .member(member)
                        .build()
        );

        return JwtTokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Map<String, Object> getClaims(String bearerToken){
        String token = bearerToken.replace("Bearer ", "");
        try {
            return (Claims) Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parse(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            throw new AccessTokenExpiredException();
        } catch (Exception e){
            throw new InvalidTokenException();
        }
    }
}
