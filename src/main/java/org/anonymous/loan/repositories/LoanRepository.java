package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LoanRepository extends JpaRepository<Loan, Long>, QuerydslPredicateExecutor<Loan> {


}
