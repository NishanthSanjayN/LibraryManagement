package com.library.Library.service;

import com.library.Library.repository.BookRepository;
import com.library.Library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // New method to handle searching
    public List<Book> findBooks(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Search by keyword in both title and author fields
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
        }
        // If no keyword is provided, return all books
        return bookRepository.findAll();
    }

    // Original findAllBooks method updated to use findBooks without a keyword
    public List<Book> findAllBooks() {
        return findBooks(null);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public Book checkInBook(Long id) {
        return bookRepository.findById(id).map(book -> {
            book.setQuantity(book.getQuantity() + 1);
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
}
