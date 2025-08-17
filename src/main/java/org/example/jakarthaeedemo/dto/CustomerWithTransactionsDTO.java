package org.example.jakarthaeedemo.dto;

import java.util.List;

public record CustomerWithTransactionsDTO(
        Long id,
        String name,
        String email,
        List<TransactionDTO> transactions
) {}
