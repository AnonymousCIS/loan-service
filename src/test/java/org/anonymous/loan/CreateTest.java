package org.anonymous.loan;

import org.anonymous.loan.services.LoanUpdateService;
import org.anonymous.loan.services.PredictService;
import org.anonymous.loan.services.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CreateTest {

    @Autowired
    private LoanUpdateService updateService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private PredictService predictService;

    @Test
    void test() {
        updateService.randomCreate(5000);
    }

    @Test
    void train() {
        trainService.train();
    }

    @Test
    void predict() {
//        List<Integer> items = new ArrayList<>();
//        items.add(1);
//        items.add(2);
//        items.add(3);
//        items.add(4);
//        items.add(5);
//        System.out.println(items);
//        List<Long> test = predictService.predict(items);
//        System.out.println(test);
    }
}
