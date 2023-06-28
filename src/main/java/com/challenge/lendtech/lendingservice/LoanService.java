package com.challenge.lendtech.lendingservice;

import com.challenge.lendtech.repository.LoanRepository;
import com.challenge.lendtech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
//    private final SMSService smsService;

    @Value("${loan.clearance.enabled:false}") // Read configuration for loan clearance
    private boolean loanClearanceEnabled;

    @Value("${loan.clearance.age.months:6}") // Read configuration for loan age in months
    private int loanClearanceAgeMonths;

    @Autowired
    public LoanService(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
//        this.smsService = smsService;
    }

    public Loan lendLoan(String msisdn, BigDecimal amount) {
        User user = userRepository.findById(msisdn).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAmount(amount);
        loan.setLoanDate(LocalDate.now());

        loan = loanRepository.save(loan);

        // Send SMS notification
//        smsService.sendSMS(msisdn, "Loan of " + amount + " has been lent.");

        return loan;
    }

    public Loan repayLoan(Long loanId, BigDecimal amount) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // Calculate remaining loan amount
        BigDecimal remainingAmount = loan.getAmount().subtract(amount);

        if (remainingAmount.compareTo(BigDecimal.ZERO) >= 0) {
            loan.setAmount(remainingAmount);
            loanRepository.save(loan);

            // Send SMS notification
//            smsService.sendSMS(loan.getUser().getMsisdn(), "Repayment of " + amount + " received. Remaining loan amount: " + remainingAmount);
        } else {
            // Send SMS notification
//            smsService.sendSMS(loan.getUser().getMsisdn(), "Repayment of " + loan.getAmount() + " received. Loan fully repaid.");

            loanRepository.delete(loan);
        }

        return loan;
    }
    @Scheduled(cron = "0 0 0 * * *") // Execute daily at midnight
    public void clearDefaultedLoans() {
        if (!loanClearanceEnabled) {
            return; // Loan clearance is disabled
        }

        LocalDate clearanceDate = LocalDate.now().minusMonths(loanClearanceAgeMonths);

        List<Loan> defaultedLoans = loanRepository.findByLoanDateBeforeAndAmountGreaterThan(clearanceDate, BigDecimal.ZERO);

        for (Loan loan : defaultedLoans) {
            // Send SMS notification
//            smsService.sendSMS(loan.getUser().getMsisdn(), "Your defaulted loan has been cleared.");

            loanRepository.delete(loan);
        }
    }
}

