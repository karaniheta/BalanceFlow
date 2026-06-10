package com.balanceflow.balanceflow_backend.test;

import com.balanceflow.balanceflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTestController {

    private final JwtService jwtService;

    @GetMapping("/test-token")
    public String testToken() {
        return jwtService.generateToken("het@gmail.com");
    }
}