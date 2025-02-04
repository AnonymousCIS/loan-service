package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.UserLoan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserLoanRepository extends JpaRepository<UserLoan, Long>, QuerydslPredicateExecutor<UserLoan> {

    @EntityGraph(attributePaths = "loan")
    Optional<UserLoan> findBySeq(Long seq);
}
