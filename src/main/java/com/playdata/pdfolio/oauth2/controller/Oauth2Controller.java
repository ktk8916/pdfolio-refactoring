package com.playdata.pdfolio.oauth2.controller;

import com.playdata.pdfolio.oauth2.domain.request.LoginRequest;
import com.playdata.pdfolio.oauth2.domain.request.SignupRequest;
import com.playdata.pdfolio.oauth2.domain.response.Oauth2Response;
import com.playdata.pdfolio.oauth2.domain.response.Oauth2StatusResponse;
import com.playdata.pdfolio.oauth2.service.Oauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;

    @PostMapping("/{provider}/login")
    public Oauth2Response login(
            @PathVariable("provider") String provider,
            @RequestBody LoginRequest loginRequest)
    {
        return oauth2Service.login(provider, loginRequest);
    }

    @PostMapping("/{provider}/signup")
    public Oauth2Response signup(
            @PathVariable("provider") String provider,
            @RequestBody SignupRequest signupRequest)
    {
        return oauth2Service.signup(provider, signupRequest);
    }

    @GetMapping("/{provider}/check")
    @ResponseStatus(HttpStatus.OK)
    public Oauth2StatusResponse check(
            @PathVariable("provider") String provider,
            @RequestParam("code") String code)
    {
        return oauth2Service.check(provider, code);
    }
}
