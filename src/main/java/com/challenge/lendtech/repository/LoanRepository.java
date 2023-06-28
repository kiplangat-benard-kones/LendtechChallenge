package com.challenge.lendtech.repository;

import com.challenge.lendtech.lendingservice.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository  extends JpaRepository <Loan, Long> {
    List<Loan> findByLoanDateBeforeAndAmountGreaterThan(LocalDate clearanceDate, BigDecimal zero);
}
