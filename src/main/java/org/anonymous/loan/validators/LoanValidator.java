package org.anonymous.loan.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.controllers.RequestLoan;
import org.anonymous.loan.repositories.LoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

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
        String mode = requestLoan.getMode();


        if (StringUtils.hasText(mode) && !mode.equals("edit")) {
            if (repository.exists(loanName)) {

                errors.rejectValue("loanName", "Exists");
            }
        }

        if (limit <= 0L) {

            errors.rejectValue("limit", "Duplicated");
        }

        if (repaymentYear <= 0L) {

            errors.rejectValue("repaymentYear", "Duplicated");
        }
    }

    public void validates(List<RequestLoan> target, Errors errors) {
        if (errors.hasErrors()) return;

        if (!target.isEmpty()) {
            for (RequestLoan loan : target) {
                validate(loan, errors);
            }
        }
    }
}