package com.playdata.pdfolio.member.domain.request;

import java.util.List;

public record UpdateRequest(
        String nickname,
        String imageUrl,
        List<String> skills) {
}
