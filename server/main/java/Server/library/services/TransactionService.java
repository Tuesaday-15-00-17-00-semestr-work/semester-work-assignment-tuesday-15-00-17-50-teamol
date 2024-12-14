package Server.library.services;

import Server.library.models.Transaction;
import Server.library.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByBookId(Long bookId) {
        return transactionRepository.findByBookId(bookId);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public void createTransaction(Long userId, Long bookId, String action) {
        // Validate input parameters
        if (userId == null || bookId == null || action == null || action.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: User ID, Book ID, and Type are required.");
        }

        // Validate transaction type
        if (!action.equalsIgnoreCase("BORROW") && !action.equalsIgnoreCase("RETURN")) {
            throw new IllegalArgumentException("Invalid transaction type. Allowed values: BORROW, RETURN");
        }

        // Create a new transaction instance and set details
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setBookId(bookId);
        transaction.setAction(action.toUpperCase()); // Ensure type is always uppercase
        transaction.setDate(java.time.LocalDateTime.now()); // Use the current date and time

        // Save the transaction to the database
        transactionRepository.save(transaction);
    }

}
