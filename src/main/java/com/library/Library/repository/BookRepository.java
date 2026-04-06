package com.library.Library.repository;

import com.library.Library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Finds books whose title OR author contains the given search term (case-insensitive).
     * The 'Containing' keyword tells JPA to use LIKE %term%.
     * The 'IgnoreCase' ensures the search is not case-sensitive.
     */
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String titleTerm, String authorTerm);
}
