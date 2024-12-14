package Server.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowReturnService {

    @Autowired
    private BookService bookService;

    @Autowired
    private TransactionService transactionService;

    public boolean borrowBook(Long user_id, Long book_id) {
        // Check if the book is available for borrowing
        if (bookService.isAvailable(book_id)) {
            // Borrow the book
            boolean bookBorrowed = bookService.updateAvailableCopies(book_id, -1); // Decrement available copies
            if (bookBorrowed) {
                // Log the transaction
                transactionService.createTransaction(user_id, book_id, "BORROW");
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(Long user_id, Long book_id) {
        // Return the book
        boolean bookReturned = bookService.updateAvailableCopies(book_id, 1); // Increment available copies
        if (bookReturned) {
            // Log the transaction
            transactionService.createTransaction(user_id, book_id, "RETURN");
            return true;
        }
        return false;
    }
}