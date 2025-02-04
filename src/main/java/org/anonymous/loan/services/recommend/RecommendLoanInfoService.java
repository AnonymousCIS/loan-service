package org.anonymous.loan.services.recommend;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.paging.Pagination;
import org.anonymous.loan.constants.BankName;
import org.anonymous.loan.constants.Category;
import org.anonymous.loan.controllers.RecommendLoanSearch;
import org.anonymous.loan.entities.QRecommendLoan;
import org.anonymous.loan.entities.RecommendLoan;
import org.anonymous.loan.exceptions.RecommendLoanNotFoundException;
import org.anonymous.loan.repositories.RecommendLoanReposotiry;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class RecommendLoanInfoService {

    private final Utils utils;

    private final MemberUtil memberUtil;

    private final ModelMapper modelMapper;

    private final HttpServletRequest request;

    private final JPAQueryFactory queryFactory;

    private final RecommendLoanReposotiry repository;

    /**
     * 추천 대출 단일 조회
     *
     * @param seq
     * @return
     */
    public RecommendLoan get(Long seq) {

        RecommendLoan item = repository.findById(seq).orElseThrow(RecommendLoanNotFoundException::new);

        return item;
    }

    /**
     * 추천 대출 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<RecommendLoan> getList(RecommendLoanSearch search) {

        int page = Math.max(search.getPage(), 1);

        int rowsPerPage = 0;

        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;

        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();

        QRecommendLoan recommendLoan = QRecommendLoan.recommendLoan;

        QLoan loan = QLoan.loan;

        // 대출 이름별 검색
        List<String> loanNames = search.getLoanName();

        if (loanNames != null && !loanNames.isEmpty()) {

            andBuilder.and(recommendLoan.loan.loanName.in(loanNames));
        }

        // 은행명별 검색
        List<BankName> bankNames = search.getBankName();

        if (bankNames != null && !bankNames.isEmpty()) {

            andBuilder.and(recommendLoan.loan.bankName.in(bankNames));
        }

        // 카테고리별 검색
        List<Category> categories = search.getCategories();

        if (categories != null && !categories.isEmpty()) {

            andBuilder.and(recommendLoan.loan.category.in(categories));
        }

        /**
         * 키워드 검색
         *
         * - sopt
         * ALL : 은행명 + 계좌 번호 + 예금주(이름 + 이메일)
         * ACCOUNTNUMBER : 계좌 번호
         * DEPOSITOR : 예금주(이름 + 이메일)
         */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {

            skey = skey.trim();

            StringExpression loanname = recommendLoan.loan.loanName;
            StringExpression email = recommendLoan.email;

            StringExpression condition = null;

            if (sopt.equals("LOANNAME")) { // 대출 이름 검색

                condition = loanname;

            } else { // 통합 검색

                condition = loanname.concat(email);
            }

            andBuilder.and(condition.contains(skey));
        }

        // 회원 이메일로 검색
        // OneToMany 안쓰는 이유 : Page 때문.. 생각보다 OneToMany 는 자주 쓰이지 않음
        List<String> emails = search.getEmail();

        if (emails != null && !emails.isEmpty()) {

            andBuilder.and(recommendLoan.email.in(emails));
        }

        /*
        if (search instanceof  RecommendLoanSearch recommendLoanSearch) {
            List<String> emails = recommendLoanSearch.getEmail();
        }
         */

        /* 검색 처리 E */

        JPAQuery<RecommendLoan> query = queryFactory.selectFrom(recommendLoan)
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
                        ? recommendLoan.loan.interestRate.desc() : recommendLoan.loan.interestRate.asc());

            } else { // 기본 정렬 조건 - 최신순

                query.orderBy(recommendLoan.createdAt.desc());
            }
        } else { // 기본 정렬 조건 - 최신순

            query.orderBy(recommendLoan.createdAt.desc());
        }
        /* 정렬 조건 처리 E */

        List<RecommendLoan> items = query.fetch();

        long total = repository.count(andBuilder);

        int ranges = utils.isMobile() ? 5 : 10;

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 현재 로그인한 회원이 추천받은 대축 목록 조회
     *
     * MyPage에서 연동
     *
     * @param search
     * @return
     */
    public ListData<RecommendLoan> getMeyList(RecommendLoanSearch search) {

        if (!memberUtil.isLogin()) return new ListData<>(List.of(), null);

        Member member = memberUtil.getMember();

        String email = member.getEmail();

        search.setEmail(List.of(email));

        return getList(search);
    }
}