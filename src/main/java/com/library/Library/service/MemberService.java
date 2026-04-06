package com.library.Library.service;

import com.library.Library.repository.MemberRepository;
import com.library.Library.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added import
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // New method to handle searching
    public List<Member> findMembers(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return memberRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
        }
        return memberRepository.findAll();
    }
    
    // Update findAllMembers to use the new findMembers method
    public List<Member> findAllMembers() {
        return findMembers(null);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    // Added @Transactional for explicit transaction boundary, although JpaRepository methods are often transactional
    @Transactional
    public void deleteMember(Long id) {
        System.out.println("Attempting to delete member ID: " + id + ". Issues will cascade delete.");
        memberRepository.deleteById(id);
    }
}
