package org.anonymous.loan.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class RecommendLoanNotFoundException extends CommonException {

    public RecommendLoanNotFoundException() {

        super("NotFound.recommendLoan", HttpStatus.NOT_FOUND);

        setErrorCode(true);
    }
}
