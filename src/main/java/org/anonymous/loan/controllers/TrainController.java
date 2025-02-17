package org.anonymous.loan.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.paging.CommonSearch;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.TrainLog;
import org.anonymous.loan.exceptions.TrainLogNotFoundException;
import org.anonymous.loan.services.PredictService;
import org.anonymous.loan.services.TrainLogInfoService;
import org.anonymous.loan.services.TrainService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Train", description = "머신러닝 학습 API")
@Profile("ml")
@RestController
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;
    private final PredictService predictService;
    private final TrainLogInfoService trainLogInfoService;

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

    @Operation(summary = "학습 로그 검색", method="GET", description = "학습한 기록을 검색한다.")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "검색번호")
    @GetMapping("/train/log")
    public JSONData logGet(@RequestParam("seq") Long seq) {
        TrainLog trainLog = trainLogInfoService.get(seq);
        if (trainLog == null) {
            throw new TrainLogNotFoundException();
        }

        return new JSONData(trainLog);
    }

    @Operation(summary = "학습 로그 검색", method="GET", description = "학습한 기록을 검색하며, List로 반환해준다.")
    @ApiResponse(responseCode = "200")
    @GetMapping("/train/logs")
    public JSONData logGetList(@ModelAttribute CommonSearch search) {
        ListData<TrainLog> trainLog = trainLogInfoService.getList(search);
        if (trainLog == null) {
            throw new TrainLogNotFoundException();
        }

        return new JSONData(trainLog);
    }
}





















