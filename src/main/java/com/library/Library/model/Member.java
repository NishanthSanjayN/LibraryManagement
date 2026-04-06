package com.library.Library.model;

import com.library.Library.model.Issue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List; // Import List for the collection

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    
    // MAPPING ADDED: Define the one-to-many relationship with Issue.
    // Assuming the Issue entity has a field named 'member' mapping back here.
    // Setting CascadeType.ALL ensures that deleting a Member cascades to associated Issues.
    // orphanRemoval=true removes Issue records when they are disassociated from the Member.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues; 
    
    // Standard constructor without ID
    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
