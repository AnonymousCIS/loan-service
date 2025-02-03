package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.UserLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserLoanRepository extends JpaRepository<UserLoan, Long>, QuerydslPredicateExecutor<UserLoan> {
}
