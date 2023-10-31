package com.playdata.pdfolio.global.security;

import com.playdata.pdfolio.jwt.JwtProvider;
import com.playdata.pdfolio.jwt.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(!isValidAuthHeader(authHeader)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if(!jwtProvider.isValidToken(token)){
            filterChain.doFilter(request, response);
            return;
        }

        TokenInfo tokenInfo = jwtProvider.extractUser(token);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                tokenInfo,
                null,
                tokenInfo.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthHeader(String authHeader){
        return StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ");
    }
}
