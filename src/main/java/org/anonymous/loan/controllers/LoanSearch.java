package org.anonymous.loan.controllers;

import lombok.Data;
import org.anonymous.global.paging.CommonSearch;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;
import org.anonymous.loan.constants.LoanType;

import java.util.List;

@Data
public class LoanSearch extends CommonSearch {

    private List<String> loanName;

    private List<BankName> bankName;

    private List<Category> categories;

    private List<LoanType> loanTypes;
}
