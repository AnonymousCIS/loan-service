package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.QLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LoanRepository extends JpaRepository<Loan, Long>, QuerydslPredicateExecutor<Loan> {

    default boolean exists(String loanName) {

        QLoan loan = QLoan.loan;

        return exists(loan.loanName.eq(loanName));
    }
}
