package Server.library.services;

import Server.library.models.Book;
import Server.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long book_id) {
        return bookRepository.findById(book_id).orElse(null);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
    public boolean isAvailable(Long book_id) {
        Book book = bookRepository.findById(book_id).orElse(null);
        return book != null && book.getAvailableCopies() > 0;
    }

    public boolean updateAvailableCopies(Long book_id, int delta) {
        Book book = bookRepository.findById(book_id).orElse(null);
        if (book != null) {
            int newAvailableCopies = book.getAvailableCopies() + delta;
            if (newAvailableCopies >= 0) {
                book.setAvailableCopies(newAvailableCopies);
                bookRepository.save(book);
                return true;
            }
        }
        return false;
    }
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long bookId, Book updatedBook) {
        return bookRepository.findById(bookId).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setTotalCopies(updatedBook.getTotalCopies());
            book.setAvailableCopies(updatedBook.getAvailableCopies());
            return bookRepository.save(book);
        }).orElse(null);
    }

    public boolean deleteBook(Long bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
            return true;
        }
        return false;
    }
}
