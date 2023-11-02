package com.playdata.pdfolio.member.domain.request;

import java.util.List;

public record SignupRequest(
        String nickname,
        String imageUrl,
        List<String> skills
) {
}
