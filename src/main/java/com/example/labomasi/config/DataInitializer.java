package com.example.labomasi.config;

import com.example.labomasi.entity.Member;
import com.example.labomasi.enums.MemberRole;
import com.example.labomasi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@ump.ac.ma"; // Use the SAME email for check and insert
        
        // Only create if NOT exists
        if (!memberRepository.existsByEmail(adminEmail)) {
            Member admin = Member.builder()
                .firstName("Admin")
                .lastName("User")
                .email(adminEmail)  // Same email here
                .password(passwordEncoder.encode("admin123"))
                .role(MemberRole.ADMIN)
                .active(true)
                .department("Administration")
                .build();
            
            memberRepository.save(admin);
            System.out.println("✅ Default admin created: " + adminEmail);
        } else {
            System.out.println("ℹ️ Admin already exists: " + adminEmail);
        }
    }
}