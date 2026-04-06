package com.library.Library.service;

import com.library.Library.repository.MemberRepository;
import com.library.Library.repository.IssueRepository;
import com.library.Library.repository.BookRepository;
import com.library.Library.model.Member;
import com.library.Library.model.Issue;
import com.library.Library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.issueRepository = issueRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    // --- Core Business Logic: Issuing a Book ---
    @Transactional
    public Issue issueBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book is out of stock.");
        }
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

        // 1. Decrease book quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        // 2. Create the issue record
        Issue issue = new Issue(book, member);
        return issueRepository.save(issue);
    }
    
    // --- Core Business Logic: Returning a Book ---
    @Transactional
    public Issue returnBook(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue transaction not found with id: " + issueId));

        if ("RETURNED".equals(issue.getStatus())) {
            throw new RuntimeException("Book already returned.");
        }
        
        // 1. Mark issue as returned
        issue.setReturnDate(LocalDate.now());
        issue.setStatus("RETURNED");
        Issue returnedIssue = issueRepository.save(issue);
        
        // 2. Increase book quantity
        Book book = returnedIssue.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        return returnedIssue;
    }

    // --- Fetch all currently issued books (FIXED: Accepts keyword) ---
    // This resolves the compilation error by accepting the expected String argument.
    public List<Issue> findCurrentlyIssuedBooks(String keyword) {
        List<Issue> issuedBooks = issueRepository.findByStatus("ISSUED");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.trim().toLowerCase();
            return issuedBooks.stream()
                .filter(issue -> 
                    // Search by Book Title
                    issue.getBook().getTitle().toLowerCase().contains(lowerKeyword) ||
                    // Search by Member Name
                    issue.getMember().getName().toLowerCase().contains(lowerKeyword)
                )
                .collect(Collectors.toList());
        }
        return issuedBooks;
    }
}
