package com.playdata.pdfolio.oauth2.domain.response;

import com.playdata.pdfolio.oauth2.domain.dto.TokenDto;
import com.playdata.pdfolio.oauth2.domain.dto.LoginInfoDto;

public record Oauth2Response(
        LoginInfoDto loginInfo,
        TokenDto token) {

}
