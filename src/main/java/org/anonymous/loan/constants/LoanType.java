package org.anonymous.loan.constants;

import lombok.Getter;

@Getter // 우선 냅두고 이따 다시 보자.
public enum LoanType {
    PersonalCheck(1, "개인체크"), // 개인체크
    PersonalCredit(2, "개인신용"), // 개인신용
    CorporateCheck(3, "법인체크"), // 법인체크
    CorporateCredit(4, "법인신용"); // 법인신용


    private final int target;
    @Getter
    private final String title;
    LoanType(int target, String title) {
        this.target = target;
        this.title = title;
    }

    public String toString() {
        return title;
    }
}
