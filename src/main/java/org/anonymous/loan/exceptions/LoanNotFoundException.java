package org.anonymous.loan.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class LoanNotFoundException extends CommonException {

    public LoanNotFoundException() {

        super("NotFound.loan", HttpStatus.NOT_FOUND);

        setErrorCode(true);
    }
}
