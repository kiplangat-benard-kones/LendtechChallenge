package com.challenge.lendtech.controller;

import com.challenge.lendtech.lendingservice.Loan;
import com.challenge.lendtech.lendingservice.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@EnableScheduling
@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/{msisdn}")
    public Loan lendLoan(@PathVariable String msisdn, @RequestParam BigDecimal amount) {
        return loanService.lendLoan(msisdn, amount);
    }
    @PostMapping("/{loanId}/repay")
    public Loan repayLoan(@PathVariable Long loanId, @RequestParam BigDecimal amount) {
        return loanService.repayLoan(loanId, amount);
    }
}
