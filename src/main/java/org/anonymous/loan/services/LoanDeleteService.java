package org.anonymous.loan.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.exceptions.LoanNotFoundException;
import org.anonymous.loan.repositories.LoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class LoanDeleteService {

    private final LoanRepository repository;

    private final LoanInfoService infoService;

    /**
     * 대출 단일 삭제
     *
     * @param seq
     * @return
     */
    public Loan delete(Long seq) {

        Loan item = infoService.get(seq);

        if (item == null) throw new LoanNotFoundException();

        repository.delete(item);

        repository.flush();

        return item;
    }

    /**
     * 대출 목록 삭제
     *
     * @param seqs
     * @return
     */
    public List<Loan> deletes(List<Long> seqs) {

        List<Loan> deleted = new ArrayList<>();

        for (Long seq : seqs) {

            Loan item = delete(seq);

            if (item != null) {

                deleted.add(item);
            }
        }

        return deleted;
    }
}