package org.anonymous.loan.services.recommend;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.exceptions.RecommendLoanNotFoundException;
import org.anonymous.loan.repositories.RecommendLoanReposotiry;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class RecommendLoanDeleteService {

    private final RecommendLoanReposotiry reposotiry;

    private final RecommendLoanInfoService infoService;

    /**
     * 추천 대출 단일 삭제
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public RecommendLoan delete(Long seq) {

        RecommendLoan item = infoService.get(seq);

        if (item == null) throw new RecommendLoanNotFoundException();

        reposotiry.delete(item);

        reposotiry.flush();

        return item;
    }

    /**
     * 추천 대출 목록 삭제
     *
     * @param seqs
     * @return
     */
    public List<RecommendLoan> deletes(List<Long> seqs) {

        List<RecommendLoan> deleted = new ArrayList<>();

        for (Long seq : seqs) {

            RecommendLoan item = delete(seq);

            if (item != null) {

                deleted.add(item);
            }
        }

        return deleted;
    }
}