package com.library.Library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: Generates a constructor with no arguments
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private int quantity; // Number of copies available

    // Standard constructor for easy creation without ID
    public Book(String title, String author, String isbn, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }
}