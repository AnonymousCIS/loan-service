package org.anonymous.loan.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.global.entities.BaseEntity;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;

/**
 * 대출 상품
 *
 */
@Data
@Entity
public class Loan extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    // 대출 이름
    private String loanName;

    // 대출 한도
    private long limit;

    // 은행 종류
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private BankName bankName; // 특성2

    // 대출 종류
    @Enumerated(EnumType.STRING)
    @Column(length=30)
    private Category category; // 특성3

    // 대출 금리 (이자율)
    private Double interestRate; // 특성4

    // 대출 설명
    @Lob
    private String loanDescription;

    // 대출 상환일
    private Long repaymentYear;

    // 일반 유저에게 공개 여부
    private boolean isOpen;

    private boolean done;

    private Long item1_limit; // 대출한도 특성1

    private int item2_BankName; // 대출한은행이름 특성2

    private int item3_category; // 대출특성 특성3

    private Long item4_interestRate; // 이자율 특성4

    private Long item5_repaymentDate; // 상환날짜 특성5

    public String getBankNameStr() {
        return bankName == null ? "" : bankName.getTitle();
    }

    public String getCategoryStr() {
        return category == null ? "" : category.getTitle();
    }
}
