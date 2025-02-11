package com.bank.repository;

import com.bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {
    boolean existsByAccountNumber(long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    List<Account> findAllByOwnerUid(String uid);

    Optional<Account> findByCodeAndOwnerUid(String code, String uid);

    Optional<Account> findByAccountNumber(Long recipientAccountNumber);

    Account findByCodeAndAccountNumber(String code, long recipientAccountNumber);
}
