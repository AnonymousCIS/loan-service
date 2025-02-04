package org.anonymous.loan.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.global.entities.BaseEntity;

/**
 * 추천 받은 대출 로그 기록
 *
 */
@Data
@Entity
public class RecommendLoan extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Loan loan;
}
