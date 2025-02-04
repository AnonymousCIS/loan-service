package org.anonymous.loan.services.userLoan;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.UserLoan;
import org.anonymous.loan.repositories.UserLoanRepository;
import org.anonymous.loan.services.LoanInfoService;
import org.anonymous.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class UserLoanUpdateService {

    private final MemberUtil memberUtil;

    private final UserLoanRepository repository;

    private final LoanInfoService loanInfoService;

    /**
     * 유저 대출 단일 등록
     *
     * @param seq
     * @return
     */
    public UserLoan process(Long seq) {

        Loan loan = loanInfoService.get(seq);

        UserLoan item = new UserLoan();

        item.setLoan(loan);
        item.setEmail(memberUtil.getMember().getEmail());

        repository.saveAndFlush(item);

        return item;
    }

    /**
     * 유저 대출 목록 등록
     *
     * @param seqs
     * @return
     */
    public List<UserLoan> process(List<Long> seqs) {

        List<UserLoan> processed = new ArrayList<>();

        for (Long seq : seqs) {

            UserLoan item = process(seq);

            if (item != null) {

                processed.add(item);
            }
        }

        return processed;
    }
}