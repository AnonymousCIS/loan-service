package org.anonymous.loan.services.userLoan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.paging.Pagination;
import org.anonymous.global.rests.JSONData;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;
import org.anonymous.loan.controllers.RecommendLoanSearch;
import org.anonymous.loan.entities.*;
import org.anonymous.loan.exceptions.LoanNotFoundException;
import org.anonymous.loan.exceptions.RecommendLoanNotFoundException;
import org.anonymous.loan.exceptions.UserLoanNotFoundException;
import org.anonymous.loan.repositories.UserLoanRepository;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class UserLoanInfoService {

    private final Utils utils;

    private final MemberUtil memberUtil;

    private final HttpServletRequest request;

    private final JPAQueryFactory queryFactory;

    private final UserLoanRepository repository;

    private final ObjectMapper om;

    /**
     * 유저 대출 단일 조회
     *
     * @param seq
     * @return
     */
    public UserLoan get(Long seq) {

        UserLoan item = repository.findById(seq).orElseThrow(UserLoanNotFoundException::new);

        ResponseEntity<JSONData> responseEntity = utils.returnData();

        try {
            String email = om.writeValueAsString(Objects.requireNonNull(responseEntity.getBody()).getData());

            if (!email.equals(item.getEmail())) {
                throw new LoanNotFoundException();
            }

        } catch (JsonProcessingException e) {
            throw new BadRequestException();
        }

        return item;
    }

    /**
     * 유저 대출 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<UserLoan> getList(RecommendLoanSearch search) {

        int page = Math.max(search.getPage(), 1);

        int rowsPerPage = 0;

        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;

        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();

        QUserLoan userLoan = QUserLoan.userLoan;

        QRecommendLoan recommendLoan = QRecommendLoan.recommendLoan;

        QLoan loan = QLoan.loan;

        // 대출 이름별 검색
        List<String> loanNames = search.getLoanName();

        if (loanNames != null && !loanNames.isEmpty()) {

            andBuilder.and(userLoan.loan.loanName.in(loanNames));
        }

        // 은행명별 검색
        List<BankName> bankNames = search.getBankName();

        if (bankNames != null && !bankNames.isEmpty()) {

            andBuilder.and(userLoan.loan.bankName.in(bankNames));
        }

        // 카테고리별 검색
        List<Category> categories = search.getCategories();

        if (categories != null && !categories.isEmpty()) {

            andBuilder.and(userLoan.loan.category.in(categories));
        }

        /**
         * 키워드 검색
         *
         * - sopt
         * ALL : 대출 이름
         * LOANNAME : 대출 이름
         */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {

            skey = skey.trim();

            StringExpression loanname = userLoan.loan.loanName;

            StringExpression condition = null;

            if (sopt.equals("LOANNAME")) { // 대출 이름 검색

                condition = loanname;

            } else { // 통합 검색

                condition = loanname;
            }

            andBuilder.and(condition.contains(skey));
        }

        // 회원 이메일로 검색
        // OneToMany 안쓰는 이유 : Page 때문.. 생각보다 OneToMany 는 자주 쓰이지 않음
        List<String> emails = search.getEmail();

        if (emails != null && !emails.isEmpty()) {

            andBuilder.and(userLoan.email.in(emails));
        }

        /* 검색 처리 E */

        JPAQuery<UserLoan> query = queryFactory.selectFrom(userLoan)
                .leftJoin(recommendLoan.loan, loan)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit);

        /* 정렬 조건 처리 S */
        String sort = search.getSort();

        if (StringUtils.hasText(sort)) {

            // 0번째 : 필드명, 1번째 : 정렬 방향
            String[] _sort = sort.split("_");

            String field = _sort[0];

            String direction = _sort[1];

            if (field.equals("interestRate")) { // 대출 금리(이자율) 순 정렬

                query.orderBy(direction.equalsIgnoreCase("DESC")
                        ? userLoan.loan.interestRate.desc() : userLoan.loan.interestRate.asc());

            } else if (field.equals("limit")) { // 대출 한도순 정렬

                query.orderBy(direction.equalsIgnoreCase("DESC")
                        ? userLoan.loan.limit.desc() : userLoan.loan.limit.asc());

            } else if (field.equals("repaymentYear")) { // 대출 상환일순 정렬

                query.orderBy(direction.equalsIgnoreCase("DESC")
                        ? userLoan.loan.repaymentYear.desc() : userLoan.loan.repaymentYear.asc());

            } else { // 기본 정렬 조건 - 최신순

                query.orderBy(userLoan.createdAt.desc());
            }
        } else { // 기본 정렬 조건 - 최신순

            query.orderBy(userLoan.createdAt.desc());
        }
        /* 정렬 조건 처리 E */

        List<UserLoan> items = query.fetch();

        long total = repository.count(andBuilder);

        int ranges = utils.isMobile() ? 5 : 10;

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 현재 로그인한 회원의 대출 목록 조회
     *
     * MyPage에서 연동
     *
     * @param search
     * @return
     */
    public ListData<UserLoan> getMyList(RecommendLoanSearch search) {

        if (!memberUtil.isLogin()) return new ListData<>(List.of(), null);

        Member member = memberUtil.getMember();

        String email = member.getEmail();

        search.setEmail(List.of(email));

        return getList(search);
    }
}