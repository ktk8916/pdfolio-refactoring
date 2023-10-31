package com.playdata.pdfolio.member.domain.request;

import java.util.List;

public record UpdateRequest(
        String nickName,
        String imageUrl,
        List<String> skills) {
}
