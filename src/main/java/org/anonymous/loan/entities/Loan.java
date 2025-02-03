package org.anonymous.loan.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;
import org.anonymous.loan.constants.LoanType;

import java.time.LocalDateTime;

/**
 * 대출 상품
 *
 */
@Data
@Entity
public class Loan {

    @Id
    @GeneratedValue
    private Long seq;

    // 대출 이름
    private String loanName;

    // 대출 한도
    private long limit;

    // 은행 종류
    private String bankName;

    // 카테고리
    private int category;

    // 대출 종류
    private int loanType;

    // 대출 설명
    @Lob
    private String loanDescription;

    // 대출 금리 (이자율)
    private long interestRate;

    // 대출 상환일
    private LocalDateTime repaymentDate;

    // 대출 종류
    @Transient
    private LoanType _loanType;

    // 은행 종류
    @Transient
    private BankName _bankName;

    // 카테고리
    @Transient
    private Category _category;

    // 일반 유저에게 공개 여부
    private boolean isOpen;

    private boolean done;
}
