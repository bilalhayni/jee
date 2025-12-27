package com.example.labomasi.repository;

import com.example.labomasi.entity.Member;
import com.example.labomasi.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Member> findByRole(MemberRole role);

    List<Member> findByActiveTrue();

    long countByActiveTrue();

    long countByRole(MemberRole role);

    List<Member> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
}