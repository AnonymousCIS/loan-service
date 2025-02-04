package org.anonymous.loan.services.userLoan;

import lombok.RequiredArgsConstructor;
import org.anonymous.loan.entities.UserLoan;
import org.anonymous.loan.exceptions.UserLoanNotFoundException;
import org.anonymous.loan.repositories.UserLoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class UserLoanDeleteService {

    private final UserLoanRepository repository;

    private final UserLoanInfoService infoService;

    /**
     * 유저 대출 단일 삭제 (일반 사용자)
     *
     * DB 에서 삭제 X
     * 현재 시간으로 DeletedAt 할당
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public UserLoan userDelete(Long seq) {

        UserLoan item = infoService.get(seq);

        if (item == null) throw new UserLoanNotFoundException();

        item.setDeletedAt(LocalDateTime.now());

        repository.saveAndFlush(item);

        return item;
    }

    /**
     * 유저 대출 목록 삭제 (일반 사용자)
     *
     * DB 에서 삭제 X
     * 현재 시간으로 DeletedAt 할당
     *
     * @param seqs
     * @return
     */
    public List<UserLoan> userDeletes(List<Long> seqs) {

        List<UserLoan> userDeleted = new ArrayList<>();

        for (Long seq : seqs) {

            UserLoan item = userDelete(seq);

            if (item != null) {

                userDeleted.add(item);
            }
        }

        return userDeleted;
    }

    /**
     * 유저 대출 단일 삭제 (관리자)
     *
     * Base Method
     *
     * @param seq
     * @return
     */
    public UserLoan delete(Long seq) {

        UserLoan item = infoService.get(seq);

        if (item == null) throw new UserLoanNotFoundException();

        repository.delete(item);

        repository.flush();

        return item;
    }

    /**
     * 유저 대출 목록 삭제 (관리자)
     *
     * @param seqs
     * @return
     */
    public List<UserLoan> deletes(List<Long> seqs) {

        List<UserLoan> deleted = new ArrayList<>();

        for (Long seq : seqs) {

            UserLoan item = delete(seq);

            if (item != null) {

                deleted.add(item);
            }
        }

        return deleted;
    }
}
