package org.anonymous.loan.repositories;

import org.anonymous.loan.entities.TrainLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TrainLogRepository extends JpaRepository<TrainLog, Long>, QuerydslPredicateExecutor<TrainLog> {
}
