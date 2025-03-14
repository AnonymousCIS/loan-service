package org.anonymous.loan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.QLoan;
import org.anonymous.loan.entities.TrainLoanLog;
import org.anonymous.loan.repositories.LoanRepository;
import org.anonymous.loan.repositories.TrainLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Lazy
@Service
@Profile("ml")
@RequiredArgsConstructor
public class TrainService {
    @Value("${python.run.path}")
    private String runPath;

    @Value("${python.script.path}")
    private String scriptPath;

    private final LoanRepository repository;
    private final TrainLogRepository trainLogRepository;

    /**
     * 훈련 데이터 조회
     *
     * @param isAll
     * @return
     */
    public List<Loan> getList(boolean isAll) {

        if (isAll) {
            return repository.findAll();
        } else {
            QLoan loan = QLoan.loan;
            return (List<Loan>) repository.findAll(loan.done.eq(false));
        }
    }

    // 매일 자정에 훈련 진행
    @Scheduled(cron="0 0 0 * * *")
    public String train() {
        try {
            TrainLoanLog trainLoanLog = new TrainLoanLog();
            trainLoanLog.setDone(true);
            log.info("훈련 시작");
            ProcessBuilder builder = new ProcessBuilder(runPath, scriptPath + "/train.py");
            Process process = builder.start();
            int code = process.waitFor();

            InputStream err = process.getErrorStream();
            String errorString = new String(err.readAllBytes(), StandardCharsets.UTF_8);
            if (!errorString.isEmpty()) {
                System.err.println("Python 오류 메시지: " + errorString);
                trainLoanLog.setDone(false);
            }

            log.info("훈련 완료: {}", code);

            trainLoanLog.setCount(repository.count());

            // 훈련 데이터 완료 처리
            QLoan loan = QLoan.loan;
            List<Loan> items = (List<Loan>)repository.findAll(loan.done.eq(false));
            items.forEach(item -> item.setDone(true));
            repository.saveAllAndFlush(items);

            trainLogRepository.saveAndFlush(trainLoanLog);

            if (code == 0) {
                return "훈련완료.";
            } else {
                return errorString;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "훈련실패";
    }
}
