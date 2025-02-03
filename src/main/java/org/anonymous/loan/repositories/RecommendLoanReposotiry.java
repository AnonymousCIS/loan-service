package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.RecommendLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RecommendLoanReposotiry extends JpaRepository<RecommendLoan, Long>, QuerydslPredicateExecutor<RecommendLoan> {
}
