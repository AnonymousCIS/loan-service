package org.anonymous.loan.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.entities.UserLoan;
import org.anonymous.loan.services.LoanDeleteService;
import org.anonymous.loan.services.LoanUpdateService;
import org.anonymous.loan.services.TrainService;
import org.anonymous.loan.services.recommend.RecommendLoanDeleteService;
import org.anonymous.loan.services.userLoan.UserLoanDeleteService;
import org.anonymous.loan.validators.LoanValidator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "AdminLoan", description = "대출 관리 어드민 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoanController {

    private final Utils utils;

    private final LoanValidator validator;

    private final LoanUpdateService loanUpdateService;

    private final LoanDeleteService loanDeleteService;

    private final UserLoanDeleteService userLoanDeleteService;

    private final RecommendLoanDeleteService recommendDeleteService;

    private final TrainService trainService;

    /**
     * 추천 대출 로그 단일 | 목록 일괄 삭제 처리
     *
     * @param seqs
     * @return
     */
    @Operation(summary = "유저 추천내역 단일, 목록 삭제", method = "DELETE", description = "유저 추천내역 대출 삭제. DB내에서 삭제된다.")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "유저 대출 번호")
    @DeleteMapping("/recommend/deletes")
    public JSONData recommendDeletes(@RequestParam("seq") List<Long> seqs) {

        List<RecommendLoan> data = recommendDeleteService.deletes(seqs);

        return new JSONData(data);
    }

    /**
     * 대출 단일 생성
     *
     * @param form
     * @param errors
     * @return
     */
    @Operation(summary = "카드 단일 생성", method = "POST", description = "대출 하나를 생성한다.")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name = "mode", description = "추가 | 수정"),
            @Parameter(name = "loanName", description = "대출 이름", required = true),
            @Parameter(name = "limit", description = "대출 한도", required = true),
            @Parameter(name = "category", description = "대출 한도", required = true, examples = {
                    @ExampleObject(name = "신용대출", value = "CREDITLOAN"),
                    @ExampleObject(name = "담보대출", value = "MORTGAGELOAN")
            }),
            @Parameter(name = "BankName", description = "은행이름", required = true, examples = {
                    @ExampleObject(name = "한국은행", value = "HANKUK"),
                    @ExampleObject(name = "국민은행", value = "KB"),
                    @ExampleObject(name = "제일은행", value = "SC"),
                    @ExampleObject(name = "한국시티은행", value = "CITY"),
                    @ExampleObject(name = "하나은행", value = "HANA"),
                    @ExampleObject(name = "신한은행", value = "SHINHAN"),
                    @ExampleObject(name = "K-뱅크", value = "KBANK"),
                    @ExampleObject(name = "카카오은행", value = "KAKAO"),
                    @ExampleObject(name = "토스은행", value = "TOSS"),
                    @ExampleObject(name = "수협은행", value = "SUHYUP"),
                    @ExampleObject(name = "부산은행", value = "BUSAN"),
                    @ExampleObject(name = "경남은행", value = "KYUNGNAM"),
                    @ExampleObject(name = "광주은행", value = "KYANGJOO"),
                    @ExampleObject(name = "전북은행", value = "JUNBOK"),
                    @ExampleObject(name = "제주은행", value = "JEJOO"),
                    @ExampleObject(name = "롯데카드", value = "LOTTE"),
                    @ExampleObject(name = "농협은행", value = "NONGHYUP"),
                    @ExampleObject(name = "삼성카드", value = "SAMSUNG"),
                    @ExampleObject(name = "현대카드", value = "HYUNDAI"),
                    @ExampleObject(name = "우리은행", value = "WOORI"),
                    @ExampleObject(name = "신협은행", value = "SINHYUP"),
                    @ExampleObject(name = "새마을금고", value = "SAEMAEULGEUMGO"),
                    @ExampleObject(name = "우체국", value = "WOOCAEKUK")
            }),
            @Parameter(name = "repaymentYear", description = "대출 년도", required = true),
            @Parameter(name = "loanDescription", description = "대출 설명", required = true),
            @Parameter(name = "interestRate", description = "대출 이자율", required = true),
            @Parameter(name = "isOpen", description = "open 여부", required = true),
    })
    @PostMapping("/create")
    public JSONData createLoans(@RequestBody @Valid RequestLoan form, Errors errors) {

        validator.validate(form, errors);

        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        Loan data = loanUpdateService.process(form);

        return new JSONData(data);
    }

    /**
     * 대출 단일 | 일괄 수정 처리
     *
     * @param forms
     * @param errors
     * @return
     */
    @Operation(summary = "카드 단일 | 일괄 수정", method = "POST", description = "대출을 단일 | 목록 수정한다.")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name = "mode", description = "추가 | 수정"),
            @Parameter(name = "loanName", description = "대출 이름", required = true),
            @Parameter(name = "limit", description = "대출 한도", required = true),
            @Parameter(name = "category", description = "대출 한도", required = true, examples = {
                    @ExampleObject(name = "신용대출", value = "CREDITLOAN"),
                    @ExampleObject(name = "담보대출", value = "MORTGAGELOAN")
            }),
            @Parameter(name = "BankName", description = "은행이름", required = true, examples = {
                    @ExampleObject(name = "한국은행", value = "HANKUK"),
                    @ExampleObject(name = "국민은행", value = "KB"),
                    @ExampleObject(name = "제일은행", value = "SC"),
                    @ExampleObject(name = "한국시티은행", value = "CITY"),
                    @ExampleObject(name = "하나은행", value = "HANA"),
                    @ExampleObject(name = "신한은행", value = "SHINHAN"),
                    @ExampleObject(name = "K-뱅크", value = "KBANK"),
                    @ExampleObject(name = "카카오은행", value = "KAKAO"),
                    @ExampleObject(name = "토스은행", value = "TOSS"),
                    @ExampleObject(name = "수협은행", value = "SUHYUP"),
                    @ExampleObject(name = "부산은행", value = "BUSAN"),
                    @ExampleObject(name = "경남은행", value = "KYUNGNAM"),
                    @ExampleObject(name = "광주은행", value = "KYANGJOO"),
                    @ExampleObject(name = "전북은행", value = "JUNBOK"),
                    @ExampleObject(name = "제주은행", value = "JEJOO"),
                    @ExampleObject(name = "롯데카드", value = "LOTTE"),
                    @ExampleObject(name = "농협은행", value = "NONGHYUP"),
                    @ExampleObject(name = "삼성카드", value = "SAMSUNG"),
                    @ExampleObject(name = "현대카드", value = "HYUNDAI"),
                    @ExampleObject(name = "우리은행", value = "WOORI"),
                    @ExampleObject(name = "신협은행", value = "SINHYUP"),
                    @ExampleObject(name = "새마을금고", value = "SAEMAEULGEUMGO"),
                    @ExampleObject(name = "우체국", value = "WOOCAEKUK")
            }),
            @Parameter(name = "repaymentYear", description = "대출 년도", required = true),
            @Parameter(name = "loanDescription", description = "대출 설명", required = true),
            @Parameter(name = "interestRate", description = "대출 이자율", required = true),
            @Parameter(name = "isOpen", description = "open 여부", required = true),
    })
    @PatchMapping("/updates")
    public JSONData update(@RequestBody @Valid List<RequestLoan> forms, Errors errors) {

//        validator.validate(forms, errors);
//
//        if (errors.hasErrors()) throw new BadRequestException(utils.getErrorMessages(errors));

        List<Loan> data = loanUpdateService.process(forms);

        return new JSONData(data);
    }

    /**
     * 대출 단일 | 목록 일괄 삭제
     *
     * @param seqs
     * @return
     */
    @Operation(summary = "대출 단일, 일괄 삭제", description = "대출 DB내에서 삭제", method = "DELETE")
    @Parameter(name = "seq", required = true, description = "대출번호")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/deletes")
    public JSONData loanDeletes(@RequestParam("seq") List<Long> seqs) {

        List<Loan> data = loanDeleteService.deletes(seqs);

        return new JSONData(data);
    }

    /**
     * 유저 대출 단일 | 목록 일괄 삭제
     *
     * @param seqs
     * @return
     */
    @Operation(summary = "유저 대출 단일, 일괄 삭제", description = "대출 카드 DB내에서 삭제", method = "DELETE")
    @Parameter(name = "seq", required = true, description = "대출번호")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/user/deletes")
    public JSONData userLoanDeletes(@RequestParam("seq") List<Long> seqs) {

        List<UserLoan> data = userLoanDeleteService.deletes(seqs);

        return new JSONData(data);
    }

    /**
     * 대출 Train
     *
     * @return
     */
    @Operation(summary = "학습", description = "대출 DB 머신러닝 학습", method = "GET")
    @ApiResponse(responseCode = "200")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/train")
    public String train() {
        return trainService.train();
    }
}