package org.anonymous.loan.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.exceptions.LoanNotFoundException;
import org.anonymous.loan.repositories.LoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class LoanInfoService {

    private final LoanRepository loanRepository;
    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;

    public Loan getCardInfo (Long seq) {

        Loan loan = loanRepository.findById(seq).orElseThrow(LoanNotFoundException::new);

        if (loan.getDeletedAt() != null) {
            throw new LoanNotFoundException();
        }

        return loan;
    }

}