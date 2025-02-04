package org.anonymous.loan.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.entities.UserLoan;
import org.anonymous.loan.services.LoanDeleteService;
import org.anonymous.loan.services.LoanUpdateService;
import org.anonymous.loan.services.recommend.RecommendLoanDeleteService;
import org.anonymous.loan.services.userLoan.UserLoanDeleteService;
import org.anonymous.loan.validators.LoanValidator;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoanController {

    private final Utils utils;

    private final LoanValidator validator;

    private final LoanUpdateService loanUpdateService;

    private final LoanDeleteService loanDeleteService;

    private final UserLoanDeleteService userLoanDeleteService;

    private final RecommendLoanDeleteService recommendDeleteService;

    /**
     * 추천 대출 로그 단일 | 목록 일괄 삭제 처리
     *
     * @param seqs
     * @return
     */
    @DeleteMapping("/recommend/deletes")
    public JSONData recommendDeletes(@RequestParam("seq") List<Long> seqs) {

        List<RecommendLoan> data = recommendDeleteService.deletes(seqs);

        return new JSONData(data);
    }

    /**
     * 대출 단일 생성
     *
     * @param form
     * @param errors
     * @return
     */
    @PostMapping("/create")
    public JSONData createLoans(@RequestBody @Valid RequestLoan form, Errors errors) {

        validator.validate(form, errors);

        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        Loan data = loanUpdateService.process(form);

        return new JSONData(data);
    }

    /**
     * 대출 단일 | 일괄 수정 처리
     *
     * @param forms
     * @param errors
     * @return
     */
    @PatchMapping("/updates")
    public JSONData update(@RequestBody @Valid List<RequestLoan> forms, Errors errors) {

        validator.validate(forms, errors);

        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        List<Loan> data = loanUpdateService.process(forms);

        return new JSONData(data);
    }

    /**
     * 대출 단일 | 목록 일괄 삭제
     *
     * @param seqs
     * @return
     */
    @DeleteMapping("/deletes")
    public JSONData loanDeletes(@RequestParam("seq") List<Long> seqs) {

        List<Loan> data = loanDeleteService.deletes(seqs);

        return new JSONData(data);
    }

    /**
     * 유저 대출 단일 | 목록 일괄 삭제
     *
     * @param seqs
     * @return
     */
    @DeleteMapping("/user/deletes")
    public JSONData userLoanDeletes(@RequestParam("seq") List<Long> seqs) {

        List<UserLoan> data = userLoanDeleteService.deletes(seqs);

        return new JSONData(data);
    }
}