package com.balanceflow.balanceflow_backend.test;

import com.balanceflow.balanceflow_backend.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final MailService mailService;

    @GetMapping("/mail-test")
    public String test() {

        mailService.sendEmail(
                "karhet10@gmail.com",
                "BalanceFlow Test",
                "Email integration working successfully!"
        );

        return "Email sent";
    }
}