package org.anonymous.loan.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.services.PredictService;
import org.anonymous.loan.services.TrainService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Profile("ml")
@RestController
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;
    private final PredictService predictService;

    @GetMapping("/train/{mode}")
    public List<Loan> train(@PathVariable("mode") String mode) {
        return trainService.getList(mode.equals("all"));
    }


    @GetMapping("/predict")
    public List<Long> predict(@RequestParam("data") String data) {
        List<Integer> items = Arrays.stream(data.split("_")).map(Integer::valueOf).toList();
        System.out.println("items:" + items);
        return predictService.predict(items);
    }
}
