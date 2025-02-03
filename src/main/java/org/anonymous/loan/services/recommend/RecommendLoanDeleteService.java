package org.anonymous.loan.services.recommend;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.RecommendLoan;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class RecommendLoanDeleteService {

    /**
     * 추천 대출 단일 삭제
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public RecommendLoan delete(Long seq) {

        return null;
    }
}