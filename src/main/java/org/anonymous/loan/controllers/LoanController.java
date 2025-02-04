package org.anonymous.loan.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.entities.UserLoan;
import org.anonymous.loan.services.LoanInfoService;
import org.anonymous.loan.services.recommend.RecommendLoanInfoService;
import org.anonymous.loan.services.userLoan.UserLoanInfoService;
import org.springframework.web.bind.annotation.*;

/**
 * 일반 사용자 & 관리자 공용 기능
 *
 */
@RestController
@RequiredArgsConstructor
public class LoanController {

    private final Utils utils;

    private final LoanInfoService loanInfoService;

    private final UserLoanInfoService userLoanInfoService;

    private final RecommendLoanInfoService recommendInfoService;

    /**
     * 추천 대출 단일 조회
     *
     * @param seq
     * @return
     */
    @GetMapping("/recommend/view/{seq}")
    public JSONData recommendView(@PathVariable("seq") Long seq) {

        RecommendLoan data = recommendInfoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 추천 대출 목록 조회
     *
     * @return
     */
    @GetMapping("/recommend/list")
    public JSONData recommendList(@ModelAttribute RecommendLoanSearch search) {

        ListData<RecommendLoan> data = recommendInfoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 대출 단일 조회
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        Loan data = loanInfoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 대출 목록 조회
     *
     * @param search
     * @return
     */
    @GetMapping("/list")
    public JSONData list(@ModelAttribute LoanSearch search) {

        ListData<Loan> data = loanInfoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 유저 대출 단일 조회
     *
     * @param seq
     * @return
     */
    @GetMapping("/user/view/{seq}")
    public JSONData userView(@PathVariable("seq") Long seq) {

        UserLoan data = userLoanInfoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 유저 대출 목록 조회
     *
     * @param search
     * @return
     */
    @GetMapping("/user/list")
    public JSONData userList(@ModelAttribute RecommendLoanSearch search) {

        ListData<UserLoan> data = userLoanInfoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 유저 대출 삭제
     *
     * 단일 | 목록 일괄 수정
     *
     * DB 삭제 X, 일반 유저 전용 삭제로 deletedAt 현재 시간으로 부여
     *
     * @return
     */
    @PatchMapping("/user/deletes")
    public JSONData userDeletes() {

        return new JSONData();
    }
}