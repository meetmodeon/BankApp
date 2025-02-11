package com.bank.repository;

import com.bank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
    Page<Transaction> findAllByAccountAccountIdAndOwnerUid(String accountId, String uid, Pageable pageable);

    Page<Transaction> findAllByCardCardIdAndOwnerUid(String cardId, String uid, Pageable pageable);

    Page<Transaction> findAllByOwnerUid(String uid, Pageable pageable);
}
