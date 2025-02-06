package org.anonymous.loan.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.entities.UserLoan;
import org.anonymous.loan.services.LoanInfoService;
import org.anonymous.loan.services.recommend.RecommendLoanInfoService;
import org.anonymous.loan.services.userLoan.UserLoanDeleteService;
import org.anonymous.loan.services.userLoan.UserLoanInfoService;
import org.anonymous.loan.services.userLoan.UserLoanUpdateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 일반 사용자 & 관리자 공용 기능
 *
 */
@RestController
@RequiredArgsConstructor
public class LoanController {

    private final Utils utils;

    private final LoanInfoService loanInfoService;

    private final UserLoanInfoService userLoanInfoService;

    private final UserLoanDeleteService userLoanDeleteService;

    private final UserLoanUpdateService userLoanUpdateService;

    private final RecommendLoanInfoService recommendInfoService;

    /**
     * 추천 대출 단일 조회
     *
     * @param seq
     * @return
     */
    @Operation(summary = "추천받은 내역 단일 조회", method="GET", description = "유저가 추천받은 내역을 조회한다.")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "번호")
    @GetMapping("/recommend/view/{seq}")
    public JSONData recommendView(@PathVariable("seq") Long seq) {

        RecommendLoan data = recommendInfoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 추천 대출 목록 조회
     *
     * @return
     */
    @Operation(summary = "유저 추천 받은 내역 조회", method="GET", description = "data - 조회된 추천받은 대출 목록, pagination - 페이징 기초 데이터")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="page", description = "페이지 번호", example = "1"),
            @Parameter(name="limit", description = "한페이지당 레코드 갯수", example = "20"),
            @Parameter(name="sopt", description = "검색옵션", example = "ALL"),
            @Parameter(name="skey", description = "검색키워드"),
            @Parameter(name="sort", description = "정렬 기준"),
            @Parameter(name="loanName", description = "대출 이름"),
            @Parameter(name = "BankName", description = "은행이름 별 검색", examples = {
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
            @Parameter(name="Category", description = "카테고리별 검색", examples = {
                    @ExampleObject(name = "신용대출", value = "CREDITLOAN"),
                    @ExampleObject(name = "담보대출", value = "MORTGAGELOAN")
            }),
            @Parameter(name="email", description = "이메일별로 검색"),
    })
    @GetMapping("/recommend/list")
    public JSONData recommendList(@ModelAttribute RecommendLoanSearch search) {

        ListData<RecommendLoan> data = recommendInfoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 대출 단일 조회
     *
     * @param seq
     * @return
     */
    @Operation(summary = "대출 단일 조회", method="GET", description = "추천 대출 조회.")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "번호")
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        Loan data = loanInfoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 대출 목록 조회
     *
     * @param search
     * @return
     */
    @Operation(summary = "추천 대출 목록 조회", method="GET", description = "data - 조회된 대출 목록, pagination - 페이징 기초 데이터")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="page", description = "페이지 번호", example = "1"),
            @Parameter(name="limit", description = "한페이지당 레코드 갯수", example = "20"),
            @Parameter(name="sopt", description = "검색옵션", example = "ALL"),
            @Parameter(name="skey", description = "검색키워드"),
            @Parameter(name="sort", description = "정렬 기준"),
            @Parameter(name="loanName", description = "대출 이름"),
            @Parameter(name = "BankName", description = "은행이름 별 검색", examples = {
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
            @Parameter(name="Category", description = "카테고리별 검색", examples = {
                    @ExampleObject(name = "신용대출", value = "CREDITLOAN"),
                    @ExampleObject(name = "담보대출", value = "MORTGAGELOAN")
            }),
    })
    @GetMapping("/list")
    public JSONData list(@ModelAttribute LoanSearch search) {

        ListData<Loan> data = loanInfoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 유저 대출 단일 조회
     *
     * @param seq
     * @return
     */
    @Operation(summary = "유저 대출 단일 조회", method="GET", description = "유저 대출 조회.")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "번호")
    @GetMapping("/user/view/{seq}")
    public JSONData userView(@PathVariable("seq") Long seq) {

        UserLoan data = userLoanInfoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 유저 대출 목록 조회
     *
     * @param search
     * @return
     */
    @Operation(summary = "유저 추천 받은 내역 조회", method="GET", description = "data - 조회된 추천받은 대출 목록, pagination - 페이징 기초 데이터")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="page", description = "페이지 번호", example = "1"),
            @Parameter(name="limit", description = "한페이지당 레코드 갯수", example = "20"),
            @Parameter(name="sopt", description = "검색옵션", example = "ALL"),
            @Parameter(name="skey", description = "검색키워드"),
            @Parameter(name="sort", description = "정렬 기준"),
            @Parameter(name="loanName", description = "대출 이름"),
            @Parameter(name = "BankName", description = "은행이름 별 검색", examples = {
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
            @Parameter(name="Category", description = "카테고리별 검색", examples = {
                    @ExampleObject(name = "신용대출", value = "CREDITLOAN"),
                    @ExampleObject(name = "담보대출", value = "MORTGAGELOAN")
            }),
            @Parameter(name="email", description = "이메일별로 검색"),
    })
    @GetMapping("/user/list")
    public JSONData userList(@ModelAttribute RecommendLoanSearch search) {

        ListData<UserLoan> data = userLoanInfoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 유저 대출 단일 | 목록 일괄 등록 처리
     *
     * @param seqs
     * @return
     */
    @Operation(summary = "유저 대출 생성", method="POST", description = "유저 대출 생성")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", description = "대출 번호")
    @PostMapping("/user/create")
    public JSONData createUserLoan(List<Long> seqs) {

        List<UserLoan> data = userLoanUpdateService.process(seqs);

        return new JSONData(data);
    }

    /**
     * 유저 대출 삭제
     *
     * 단일 | 목록 일괄 수정
     *
     * DB 삭제 X, 일반 유저 전용 삭제로 deletedAt 현재 시간으로 부여
     *
     * @param seqs
     * @return
     */
    @Operation(summary = "유저 대출 목록 삭제", method = "PATCH")
    @ApiResponse(responseCode = "200", description = "유저 대출 삭제. 실제로 DB 내에서는 지워지는게 아님.")
    @Parameter(name="seq", description = "유저 대출 번호")
    @PatchMapping("/user/deletes")
    public JSONData userDeletes(@RequestParam("seq") List<Long> seqs) {

        List<UserLoan> data = userLoanDeleteService.userDeletes(seqs);

        return new JSONData(data);
    }
}