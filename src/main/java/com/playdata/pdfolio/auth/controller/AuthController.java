package com.playdata.pdfolio.auth.controller;

import com.playdata.pdfolio.auth.service.AuthService;
import com.playdata.pdfolio.member.domain.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/{provider}")
    public AuthResponse authenticate(@PathVariable("provider") String provider, @RequestParam("code") String code){
        return authService.authenticate(provider, code);
    }
}
