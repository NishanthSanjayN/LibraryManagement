package com.library.Library.Controller;

import com.library.Library.model.Book;
import com.library.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final BookService bookService;

    @Autowired
    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    // NEW: Maps the root URL to the dedicated homepage (index.html)
    @GetMapping("/")
    public String viewHomePage() {
        // You will need to create an index.html file for this
        return "index";
    }

    // UPDATED: Renamed the book listing method and changed the mapping to /books
    // This provides a cleaner separation from the root index page.
    @GetMapping("/books")
    public String listBooks(@RequestParam(required = false) String keyword, Model model) {
        
        // Use the new findBooks method to search or return all
        model.addAttribute("books", bookService.findBooks(keyword));
        
        // Add the keyword back to the model so the search bar retains the input
        model.addAttribute("keyword", keyword); 
        
        return "books"; 
    }

    @GetMapping("/addbook")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book"; 
    }

    @PostMapping("/addbook")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        // Changed redirect from / to /books
        return "redirect:/books"; 
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Book book = bookService.findBookById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        
        model.addAttribute("book", book);
        return "update-book"; 
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable("id") long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.saveBook(book); 
        // Changed redirect from / to /books
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteBook(id);
        // Changed redirect from / to /books
        return "redirect:/books";
    }
}