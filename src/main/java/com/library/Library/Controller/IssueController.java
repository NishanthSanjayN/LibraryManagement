package com.library.Library.Controller;

import com.library.Library.service.BookService;
import com.library.Library.model.Issue;
import com.library.Library.service.IssueService;
import com.library.Library.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;
    private final BookService bookService;
    private final MemberService memberService;

    public IssueController(IssueService issueService, BookService bookService, MemberService memberService) {
        this.issueService = issueService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    // --- READ: Show all currently issued books (GET /issues) ---
    // UPDATED: Now accepts an optional 'keyword' parameter for searching
    @GetMapping
    public String listIssuedBooks(@RequestParam(required = false) String keyword, Model model) {
        // Pass the keyword to the service for filtering
        List<Issue> issuedBooks = issueService.findCurrentlyIssuedBooks(keyword);
        model.addAttribute("issues", issuedBooks);
        model.addAttribute("keyword", keyword); // Pass keyword back to view to retain search bar content
        return "issues/list"; 
    }

    // --- CREATE (Part 1): Show the form for issuing a book (GET /issues/issue) ---
    @GetMapping("/issue")
    public String showIssueForm(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        model.addAttribute("members", memberService.findAllMembers());
        // Simple form object to hold the IDs
        model.addAttribute("issueForm", new IssueForm()); 
        return "issues/issue-form"; 
    }

    // --- CREATE (Part 2): Handle the form submission (POST /issues/issue) ---
    @PostMapping("/issue")
    public String issueBook(@ModelAttribute IssueForm issueForm, RedirectAttributes redirectAttributes) {
        try {
            issueService.issueBook(issueForm.getBookId(), issueForm.getMemberId());
            redirectAttributes.addFlashAttribute("successMessage", "Book successfully issued!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/issues"; 
    }

    // --- RETURN: Handle the return action (GET /issues/return/{id}) ---
    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            issueService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book successfully returned!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/issues";
    }
    
    // Simple inner class to handle form data for issuing
    public static class IssueForm {
        private Long bookId;
        private Long memberId;

        // Getters and setters (required for form binding)
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getMemberId() { return memberId; }
        public void setMemberId(Long memberId) { this.memberId = memberId; }
    }
}
