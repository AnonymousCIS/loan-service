package org.anonymous.loan.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.global.entities.BaseMemberEntity;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserLoan extends BaseMemberEntity {

    @Id
    @GeneratedValue
    private Long seq;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Loan loan;
}