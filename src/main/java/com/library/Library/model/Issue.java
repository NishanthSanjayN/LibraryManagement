package com.library.Library.model;

import com.library.Library.model.Book;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with Book
    // FetchType.LAZY improves performance by only loading the Book object when explicitly accessed.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Many-to-one relationship with Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDate issueDate = LocalDate.now();
    // Default due date is set to two weeks from the issue date
    private LocalDate dueDate = LocalDate.now().plusWeeks(2);
    
    private LocalDate returnDate; // Null until the book is returned
    
    // Status used for filtering: ISSUED or RETURNED
    private String status = "ISSUED"; 

    // Constructor for new issues
    public Issue(Book book, Member member) {
        this.book = book;
        this.member = member;
    }
}
