package org.anonymous.loan.services;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;
import org.anonymous.loan.controllers.RequestLoan;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.exceptions.LoanNotFoundException;
import org.anonymous.loan.repositories.LoanRepository;
import org.anonymous.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Lazy
@Service
@RequiredArgsConstructor
public class LoanUpdateService {

    private final MemberUtil memberUtil;

    private final LoanRepository loanRepository;

    /**
     * 대출 랜덤 생성
     */
    public void randomCreate(int count) {
        List<Loan> loans = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Loan loan = new Loan();
            Faker faker = new Faker();
            Random random = new Random();
            loan.setLoanName(faker.commerce().material() + "대출");
            loan.setLimit(random.nextLong(1000) * 1000000L); // 100만원부터 10억까지
            loan.setItem1_limit(loan.getLimit() / 1000000L);
            loan.setBankName(BankName.values()[random.nextInt(BankName.values().length)]);
            loan.setItem2_BankName(loan.getBankName().getTarget());
            loan.setCategory(Category.values()[random.nextInt(Category.values().length)]);
            loan.setItem3_category(loan.getCategory().getTarget());
            loan.setInterestRate(Math.round(random.nextDouble(0.1, 10.0) * 100) / 100.0);
            loan.setItem4_interestRate(Math.round(loan.getInterestRate()));
            // 이름, 대출종류, 금리, 대출금액
            loan.setLoanDescription(String.format("%s는 %s로 %s%%의 금리로 %s원의 금액까지 제공합니다.", loan.getLoanName(), loan.getCategory().getTitle(), loan.getInterestRate(), loan.getLimit()));
            long repaymentDate = random.nextLong(50);
            loan.setRepaymentYear(repaymentDate);
            loan.setItem5_repaymentDate(repaymentDate);
            loan.setOpen(true);
            loan.setDone(false);
            loans.add(loan);
        }

        loanRepository.saveAllAndFlush(loans);
    }

    /**
     * 대출 단일 등록 | 수정
     *
     * @param form
     * @return
     */
    public Loan process(RequestLoan form) {

        if (!memberUtil.isAdmin()) throw new UnAuthorizedException();

        Long seq = Objects.requireNonNullElse(form.getSeq(), 0L);

        String mode = Objects.requireNonNullElse(form.getMode(), "add");

        Loan data = null;

        if (mode.equals("edit")) { // 대출 수정

            data = loanRepository.findById(seq).orElseThrow(LoanNotFoundException::new);

        } else { // 대출 등록

            /**
             * 신규 대출 등록시 최초 한번만 기록되는 데이터
             * - 대출 이름
             * - 은행명
             */

            data = new Loan();

            data.setBankName(form.getBankName());
            data.setLoanName(form.getLoanName());
        }

        /* 신규 대출 등록 & 수정 공통 반영 사항 S */

        data.setLoanName(form.getLoanName());
        data.setCategory(form.getCategory());
        data.setLimit(form.getLimit());
        data.setInterestRate(form.getInterestRate());
        data.setLoanDescription(form.getLoanDescription());
        data.setRepaymentYear(form.getRepaymentYear());
        data.setOpen(form.isOpen());
        data.setDone(false);

        data.setItem1_limit(form.getLimit() / 1000000L);
        data.setItem2_BankName(form.getBankName().getTarget());
        data.setItem3_category(form.getCategory().getTarget());
        data.setItem4_interestRate(Math.round(form.getInterestRate()));
        data.setItem5_repaymentDate(form.getRepaymentYear());

        loanRepository.saveAndFlush(data);

        return data;
        /* 신규 대출 등록 & 수정 공통 반영 사항 E */
    }

    /**
     * 대출 목록 등록 | 수정
     *
     * @param forms
     * @return
     */
    public List<Loan> process(List<RequestLoan> forms) {

        List<Loan> processed = new ArrayList<>();

        for (RequestLoan form : forms) {

            Loan item = process(form);

            if (item != null) {

                processed.add(item);
            }
        }

        return processed;
    }
}

















