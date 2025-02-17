package org.anonymous.loan.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.anonymous.global.entities.BaseMemberEntity;

@Data
@Entity
public class TrainLoanLog extends BaseMemberEntity {

    @Id @GeneratedValue
    private Long seq;

    private Long count;

    private boolean done;
}
