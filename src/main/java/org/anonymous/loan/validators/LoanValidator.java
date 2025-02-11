package org.anonymous.loan.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.controllers.RequestLoan;
import org.anonymous.loan.repositories.LoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class LoanValidator implements Validator {

    private final LoanRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(RequestLoan.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) return;

        RequestLoan requestLoan = (RequestLoan) target;

        String loanName = requestLoan.getLoanName();
        Long limit = requestLoan.getLimit();
        Long repaymentYear = requestLoan.getRepaymentYear();

        if (repository.exists(loanName)) {

            errors.rejectValue("loanName", "Exists");
        }

        if (limit <= 0L) {

            errors.rejectValue("limit", "Duplicated");
        }

        if (repaymentYear <= 0L) {

            errors.rejectValue("repaymentYear", "Duplicated");
        }
    }
}