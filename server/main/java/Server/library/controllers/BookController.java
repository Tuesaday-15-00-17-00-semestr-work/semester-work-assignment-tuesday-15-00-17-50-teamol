package Server.library.controllers;

import Server.library.models.Book;
import Server.library.services.BookService;
import Server.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{book_id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long book_id) {
        Book book = bookService.getBookById(book_id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            Book savedBook = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return a 400 if something goes wrong
        }
    }

    @PutMapping("/update/{book_id}")
    public ResponseEntity<?> updateBook(@PathVariable Long book_id, @RequestBody Book updatedBook, HttpServletRequest request) {
        Book book = bookService.updateBook(book_id, updatedBook);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.status(404).body("Book not found.");
    }

    @DeleteMapping("/delete/{book_id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long book_id, HttpServletRequest request) {
        boolean isDeleted = bookService.deleteBook(book_id);
        if (isDeleted) {
            return ResponseEntity.ok("Book deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Book not found.");
        }
    }
}

