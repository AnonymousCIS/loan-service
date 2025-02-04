package org.anonymous.loan.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;

@Data
public class RequestLoan {

    private Long seq;

    private String mode; // 추가 | 수정

    @NotBlank
    private String loanName;

    @NotBlank
    private long limit;

    @NotBlank
    private Category category;

    @NotBlank
    private BankName bankName;

    @NotBlank
    private Long repaymentYear;

    @NotBlank
    private String loanDescription;

    @NotBlank
    private Double interestRate;

    @NotBlank
    private boolean isOpen;
}
