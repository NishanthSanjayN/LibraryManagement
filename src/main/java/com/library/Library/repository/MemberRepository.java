package com.library.Library.repository;

import com.library.Library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    /**
     * Finds members whose name OR email contains the given search term (case-insensitive).
     */
    List<Member> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nameTerm, String emailTerm);
}