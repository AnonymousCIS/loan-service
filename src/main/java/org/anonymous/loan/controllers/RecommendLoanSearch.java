package org.anonymous.loan.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RecommendLoanSearch extends LoanSearch {

    private List<String> email;

    private String mode;
}
