package org.anonymous.loan.services;

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
import org.anonymous.loan.controllers.LoanSearch;
import org.anonymous.loan.controllers.RequestLoan;
import org.anonymous.loan.entities.Loan;
import org.anonymous.loan.entities.QLoan;
import org.anonymous.loan.exceptions.LoanNotFoundException;
import org.anonymous.loan.repositories.LoanRepository;
import org.anonymous.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class LoanInfoService {

    private final Utils utils;

    private final MemberUtil memberUtil;

    private final ModelMapper modelMapper;

    private final HttpServletRequest request;

    private final JPAQueryFactory queryFactory;

    private final LoanRepository loanRepository;

    /**
     * 대출 단일 조회
     *
     * @param seq
     * @return
     */
    public Loan get(Long seq) {

        Loan loan = loanRepository.findById(seq).orElseThrow(LoanNotFoundException::new);

        if (!memberUtil.isAdmin() && !loan.isOpen()) throw new LoanNotFoundException();

        return loan;
    }


    /**
     * 대출 수정시 필요한 커맨드 객체 RequestLoan 로 변환해 반환
     *
     * @param seq
     * @return
     */
    public RequestLoan getForm(Long seq) {

        return getForm(get(seq));
    }

    /**
     * 대출 수정시 필요한 커맨드 객체 RequestLoan 로 변환해 반환
     *
     * Base method
     *
     * @param item
     * @return
     */
    public RequestLoan getForm(Loan item) {

        RequestLoan form = modelMapper.map(item, RequestLoan.class);

        form.setMode("edit");

        return form;
    }

    /**
     * 대출 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Loan> getList(LoanSearch search) {

        int page = Math.max(search.getPage(), 1);

        int rowsPerPage = 0;

        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;

        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();

        QLoan loan = QLoan.loan;

        // 대출 이름별 검색
        List<String> loanNames = search.getLoanName();

        if (loanNames != null && !loanNames.isEmpty()) {

            andBuilder.and(loan.loanName.in(loanNames));
        }

        // 은행명별 검색
        List<BankName> bankNames = search.getBankName();

        if (bankNames != null && !bankNames.isEmpty()) {

            andBuilder.and(loan.bankName.in(bankNames));
        }

        // 카테고리별 검색
        List<Category> categories = search.getCategories();

        if (categories != null && !categories.isEmpty()) {

            andBuilder.and(loan.category.in(categories));
        }

        if (!memberUtil.isAdmin()) andBuilder.and(loan.isOpen);

        /**
         * 키워드 검색
         *
         * - sopt
         * ALL : 대출 설명
         * LOANDESCRIPTION : 대출 설명
         */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {

            skey = skey.trim();

            StringExpression loanDescription = loan.loanDescription;

            StringExpression condition = null;

            if (sopt.equals("LOANDESCRIPTION")) { // 대출 이름 검색

                condition = loanDescription;

            } else { // 통합 검색

                condition = loanDescription;
            }

            andBuilder.and(condition.contains(skey));
        }
        /* 검색 처리 E */

        JPAQuery<Loan> query = queryFactory.selectFrom(loan)
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
                        ? loan.interestRate.desc() : loan.interestRate.asc());

            } else if (field.equals("limit")) { // 대출 한도순 정렬

                query.orderBy(direction.equalsIgnoreCase("DESC")
                        ? loan.limit.desc() : loan.limit.asc());

            } else if (field.equals("repaymentYear")) { // 대출 상환일순 정렬

                query.orderBy(direction.equalsIgnoreCase("DESC")
                        ? loan.repaymentYear.desc() : loan.repaymentYear.asc());

            } else { // 기본 정렬 조건 - 최신순

                query.orderBy(loan.createdAt.desc());
            }
        } else { // 기본 정렬 조건 - 최신순

            query.orderBy(loan.createdAt.desc());
        }
        /* 정렬 조건 처리 E */

        List<Loan> items = query.fetch();

        long total = loanRepository.count(andBuilder);

        int ranges = utils.isMobile() ? 5 : 10;

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }
}