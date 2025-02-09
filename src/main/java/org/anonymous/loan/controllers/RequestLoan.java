package org.anonymous.loan.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;

@Data
public class RequestLoan {

    private Long seq;

    private String mode; // 추가 | 수정

    @NotBlank
    private String loanName;

    @NotNull
    private Long limit;

    @NotNull
    private Category category;

    @NotNull
    private BankName bankName;

    @NotNull
    private Long repaymentYear;

    @NotBlank
    private String loanDescription;

    @NotNull
    private Double interestRate;

    private boolean isOpen;
}
