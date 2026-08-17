package com.alonsomoros.tfg.domain.port;

import com.alonsomoros.tfg.domain.model.Transaction;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
}
