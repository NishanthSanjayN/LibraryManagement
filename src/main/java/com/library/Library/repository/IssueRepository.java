package com.library.Library.repository;

import com.library.Library.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    
    // Custom query to find all currently issued (not returned) books
    List<Issue> findByStatus(String status);
}
