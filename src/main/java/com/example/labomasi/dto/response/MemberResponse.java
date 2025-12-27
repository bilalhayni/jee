package com.example.labomasi.dto.response;

import com.example.labomasi.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String initials;

    private String email;

    private String phone;

    private String department;

    private String specialization;

    private MemberRole role;

    private String roleDisplayName;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
