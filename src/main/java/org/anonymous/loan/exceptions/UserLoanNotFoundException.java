package org.anonymous.loan.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class UserLoanNotFoundException extends CommonException {

    public UserLoanNotFoundException() {

        super("NotFound.userLoan", HttpStatus.FOUND);

        setErrorCode(true);
    }
}
