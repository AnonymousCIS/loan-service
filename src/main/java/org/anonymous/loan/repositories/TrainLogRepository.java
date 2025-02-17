package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.TrainLoanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TrainLogRepository extends JpaRepository<TrainLoanLog, Long>, QuerydslPredicateExecutor<TrainLoanLog> {
}
