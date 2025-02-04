package org.anonymous.loan.services;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.repositories.LoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Lazy
@Service
@RequiredArgsConstructor
public class LoanUpdateService {

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
            loan.setRepaymentDate(LocalDateTime.now().plusYears(repaymentDate));
            loan.setItem5_repaymentDate(repaymentDate);
            loan.setOpen(true);
            loan.setDone(false);
            loans.add(loan);
        }

        loanRepository.saveAllAndFlush(loans);
    }
}

















