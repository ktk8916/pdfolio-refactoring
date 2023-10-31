package com.playdata.pdfolio.oauth2.domain.request;

import java.util.List;

public record SignupRequest(
        String accessToken,
        String nickName,
        String imageUrl,
        List<String> skills
) {
}
