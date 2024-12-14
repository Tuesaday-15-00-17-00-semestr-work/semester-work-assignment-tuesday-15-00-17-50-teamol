package Server.library.repositories;

import Server.library.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Fix the query to properly reference the bookId field in the Book entity
    List<Transaction> findByBookId(Long bookId);

    List<Transaction> findByUserId(Long userId);
}