package com.cth.tradefinance.repository;

import com.cth.tradefinance.model.TradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
    Optional<TradeTransaction> findByTransactionRef(String transactionRef);
}
