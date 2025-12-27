package com.example.labomasi.config;

import com.example.labomasi.entity.Member;
import com.example.labomasi.enums.MemberRole;
import com.example.labomasi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfNotExists(
            "admin@ump.ac.ma",
            "admin123",
            "Admin",
            "User",
            MemberRole.ADMIN,
            "Administration"
        );

        createUserIfNotExists(
            "director@ump.ac.ma",
            "director123",
            "Lab",
            "Director",
            MemberRole.DIRECTOR,
            "Laboratory Direction"
        );

        createUserIfNotExists(
            "teacher@ump.ac.ma",
            "teacher123",
            "Teacher",
            "User",
            MemberRole.TEACHER,
            "Computer Science"
        );

        createUserIfNotExists(
            "doctorant@ump.ac.ma",
            "doctorant123",
            "PhD",
            "Student",
            MemberRole.DOCTORANT,
            "Research"
        );
    }

    private void createUserIfNotExists(String email, String password, String firstName,
                                        String lastName, MemberRole role, String department) {
        if (!memberRepository.existsByEmail(email)) {
            Member member = Member.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .active(true)
                .department(department)
                .build();

            memberRepository.save(member);
            log.info("Created default {}: {}", role.getDisplayName(), email);
        } else {
            log.debug("{} already exists: {}", role.getDisplayName(), email);
        }
    }
}
