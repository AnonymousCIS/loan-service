package org.anonymous.loan.controllers;

import lombok.Data;
import org.anonymous.global.paging.CommonSearch;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;

import java.util.List;

@Data
public class LoanSearch extends CommonSearch {

    private String sort;

    private List<String> loanName;

    private List<BankName> bankName;

    private List<Category> categories;
}
