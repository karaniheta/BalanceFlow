package com.balanceflow.balanceflow_backend.auth.service;

import com.balanceflow.balanceflow_backend.auth.dto.OtpResponse;
import com.balanceflow.balanceflow_backend.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final MailService mailService;

    public OtpResponse sendOtp(String email) {

        String otp = String.valueOf(
                100000 + new Random().nextInt(900000)
        );

        redisTemplate.opsForValue().set(
                "otp:" + email,
                otp,
                Duration.ofMinutes(5)
        );

        mailService.sendEmail(
                email,
                "BalanceFlow OTP",
                "Your OTP is: " + otp
        );

        return new OtpResponse(
                true,
                "OTP sent successfully"
        );
    }

    public OtpResponse verifyOtp(
            String email,
            String otp
    ) {

        String savedOtp =
                redisTemplate.opsForValue()
                        .get("otp:" + email);

        if (savedOtp == null) {
            throw new RuntimeException(
                    "OTP expired. Please request a new OTP."
            );
        }

        if (!savedOtp.equals(otp)) {
            throw new RuntimeException(
                    "Incorrect OTP. Please try again."
            );
        }

        redisTemplate.delete(
                "otp:" + email
        );

        redisTemplate.opsForValue().set(
                "verified:" + email,
                "true",
                Duration.ofMinutes(10)
        );

        return new OtpResponse(
                true,
                "OTP verified successfully"
        );
    }
}