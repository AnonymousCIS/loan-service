package org.anonymous.loan.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
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

@Tag(name = "Train", description = "머신러닝 학습 API")
@Profile("ml")
@RestController
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;
    private final PredictService predictService;

    @Operation(summary = "train 조회", method="GET", description = "train DB 조회")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="mode", description = "string 문자열"),
    })
    @GetMapping("/train/{mode}")
    public List<Loan> train(@PathVariable("mode") String mode) {
        return trainService.getList(mode.equals("all"));
    }


    @Operation(summary = "예측", method="GET", description = "카드의 최근접 이웃 회귀 5개의 데이터 return. only 특성으로 수치화를 해서 넘겨줘야한다.")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="item1_limit", description = "대출한도"),
            @Parameter(name="item2_BankName", description = "은행이름"),
            @Parameter(name="item3_category", description = "대출특성"),
            @Parameter(name="item4_interestRate", description = "이자율"),
            @Parameter(name="item5_repaymentDate", description = "상환날짜"),
    })
    @GetMapping("/predict")
    public JSONData predict(@RequestParam("data") String data) {
        List<Integer> items = Arrays.stream(data.split("_")).map(Integer::valueOf).toList();
        System.out.println("items:" + items);
        List<Loan> loans = predictService.predict(items);
        return new JSONData(loans);
    }
}
