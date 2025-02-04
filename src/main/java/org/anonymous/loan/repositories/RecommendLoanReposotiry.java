package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.RecommendLoan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface RecommendLoanReposotiry extends JpaRepository<RecommendLoan, Long>, QuerydslPredicateExecutor<RecommendLoan> {

    @EntityGraph(attributePaths = "loan")
    Optional<RecommendLoan> findBySeq(Long seq);
}
