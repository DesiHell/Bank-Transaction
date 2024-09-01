package com.hell.demoSpringBatch.repository;

import com.hell.demoSpringBatch.dao.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
}
