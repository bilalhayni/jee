package com.example.labomasi.service;

import com.example.labomasi.entity.Member;
import com.example.labomasi.enums.MemberRole;
import com.example.labomasi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member save(Member member) {
        // Encode password if it's a new member or password changed
        if (member.getId() == null ||
                (member.getPassword() != null && !member.getPassword().startsWith("$2a$"))) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        return memberRepository.save(member);
    }

    public Member update(Long id, Member memberDetails) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setFirstName(memberDetails.getFirstName());
        member.setLastName(memberDetails.getLastName());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        member.setDepartment(memberDetails.getDepartment());
        member.setSpecialization(memberDetails.getSpecialization());
        member.setRole(memberDetails.getRole());
        member.setActive(memberDetails.isActive());

        // Only update password if provided
        if (memberDetails.getPassword() != null && !memberDetails.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(memberDetails.getPassword()));
        }

        return memberRepository.save(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public List<Member> findByRole(MemberRole role) {
        return memberRepository.findByRole(role);
    }

    public long count() {
        return memberRepository.count();
    }

    public List<Member> search(String keyword) {
        return memberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                keyword, keyword);
    }
}