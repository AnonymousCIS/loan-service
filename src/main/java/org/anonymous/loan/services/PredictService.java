package org.anonymous.loan.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.repositories.RecommendLoanReposotiry;
import org.anonymous.member.MemberUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@Profile("ml")
@RequiredArgsConstructor
public class PredictService {

    @Value("${python.run.path}")
    private String runPath;

    @Value("${python.script.path}")
    private String scriptPath;

    private final ObjectMapper om;

    private final RecommendLoanReposotiry recommendCardRepository;
    private final LoanInfoService loanInfoService;
    private final MemberUtil memberUtil;

    public List<Loan> predict(List<Integer> items) {
        try {
            String data = om.writeValueAsString(items);

            ProcessBuilder builder = new ProcessBuilder(runPath, scriptPath + "/predict_KNeighbors.py", data);
            Process process = builder.start();

            int exitCode = process.waitFor();

            InputStream in = process.getInputStream();

            InputStream err = process.getErrorStream();
            String errorString = new String(err.readAllBytes(), StandardCharsets.UTF_8);
            if (!errorString.isEmpty()) {
                System.err.println("Python 오류 메시지: " + errorString);
            }

            List<Long> results = om.readValue(in.readAllBytes(), new TypeReference<>() {});
            List<Loan> loans = new ArrayList<>();
            List<RecommendLoan> recommendLoans = new ArrayList<>();
            for (Long result : results) {
                RecommendLoan recommendLoan = new RecommendLoan();
                recommendLoan.setEmail(memberUtil.getMember().getEmail());
                Loan loan = loanInfoService.get(result);
                if (!loan.isOpen()) {
                    continue;
                }
                recommendLoan.setLoan(loan);
                loans.add(loan);
                recommendLoans.add(recommendLoan);
            }
            recommendCardRepository.saveAllAndFlush(recommendLoans);
            return loans;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.of();
    }
}
