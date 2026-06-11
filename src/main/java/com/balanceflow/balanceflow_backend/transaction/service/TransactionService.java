package com.balanceflow.balanceflow_backend.transaction.service;

import com.balanceflow.balanceflow_backend.category.entity.Category;
import com.balanceflow.balanceflow_backend.category.repository.CategoryRepository;
import com.balanceflow.balanceflow_backend.transaction.dto.CreateTransactionRequest;
import com.balanceflow.balanceflow_backend.transaction.dto.TransactionResponse;
import com.balanceflow.balanceflow_backend.transaction.entity.Transaction;
import com.balanceflow.balanceflow_backend.transaction.repository.TransactionRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.balanceflow.balanceflow_backend.transaction.dto.DeleteResponse;
import java.util.List;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionResponse create(
            CreateTransactionRequest request,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Category category = categoryRepository.findById(
                request.getCategoryId()
        ).orElseThrow();

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .note(request.getNote())
                .transactionDate(request.getTransactionDate())
                .user(user)
                .category(category)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return TransactionResponse.builder()
                .id(saved.getId())
                .amount(saved.getAmount())
                .note(saved.getNote())
                .transactionDate(saved.getTransactionDate())
                .categoryName(saved.getCategory().getName())
                .categoryType(saved.getCategory().getType())
                .build();
    }

    public List<TransactionResponse> getAll(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        List<Transaction> transactions =
                transactionRepository.findByUserId(user.getId());

        return transactions.stream()
                .map(transaction -> TransactionResponse.builder()
                        .id(transaction.getId())
                        .amount(transaction.getAmount())
                        .note(transaction.getNote())
                        .transactionDate(transaction.getTransactionDate())
                        .categoryName(transaction.getCategory().getName())
                        .categoryType(transaction.getCategory().getType())
                        .build())
                .toList();
    }

    public TransactionResponse update(
            Long transactionId,
            CreateTransactionRequest request,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow();

        transaction.setAmount(request.getAmount());
        transaction.setNote(request.getNote());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);

        return TransactionResponse.builder()
                .id(updated.getId())
                .amount(updated.getAmount())
                .note(updated.getNote())
                .transactionDate(updated.getTransactionDate())
                .categoryName(updated.getCategory().getName())
                .categoryType(updated.getCategory().getType())
                .build();
    }

    public DeleteResponse delete(
            Long transactionId,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.delete(transaction);

        return new DeleteResponse(
                "Transaction deleted successfully"
        );
    }
    public List<TransactionResponse> getByCategory(
            String email,
            Long categoryId
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return transactionRepository
                .findByUserIdAndCategoryId(
                        user.getId(),
                        categoryId
                )
                .stream()
                .map(transaction -> TransactionResponse.builder()
                        .id(transaction.getId())
                        .amount(transaction.getAmount())
                        .note(transaction.getNote())
                        .transactionDate(transaction.getTransactionDate())
                        .categoryName(
                                transaction.getCategory().getName()
                        )
                        .categoryType(
                                transaction.getCategory().getType()
                        )
                        .build()
                )
                .toList();
    }
}