package Server.library.controllers;

import Server.library.services.BorrowReturnService;
import Server.library.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/borrow-return")
public class BorrowReturnController {

    @Autowired
    private BorrowReturnService borrowReturnService;

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("user_id");
        Long bookId = payload.get("book_id");

        if (userId == null || bookId == null) {
            return ResponseEntity.badRequest().body("User ID and Book ID are required.");
        }

        boolean success = borrowReturnService.borrowBook(userId, bookId); // Add logic to handle borrowing
        return success ? ResponseEntity.ok("Book borrowed successfully.")
                : ResponseEntity.badRequest().body("Book borrowing failed.");
    }

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("user_id");
        Long bookId = payload.get("book_id");

        if (userId == null || bookId == null) {
            return ResponseEntity.badRequest().body("User ID and Book ID are required.");
        }

        boolean success = borrowReturnService.returnBook(userId, bookId); // Add logic to handle returning
        return success ? ResponseEntity.ok("Book returned successfully.")
                : ResponseEntity.badRequest().body("Book returning failed.");
    }
}