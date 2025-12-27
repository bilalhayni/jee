package com.example.labomasi.mapper;

import com.example.labomasi.dto.request.MemberRequest;
import com.example.labomasi.dto.response.MemberResponse;
import com.example.labomasi.entity.Member;
import com.example.labomasi.enums.MemberRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberMapper {

    public MemberResponse toResponse(Member member) {
        if (member == null) {
            return null;
        }

        return MemberResponse.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .fullName(member.getFullName())
                .initials(member.getInitials())
                .email(member.getEmail())
                .phone(member.getPhone())
                .department(member.getDepartment())
                .specialization(member.getSpecialization())
                .role(member.getRole())
                .roleDisplayName(member.getRole() != null ? member.getRole().getDisplayName() : null)
                .active(member.isActive())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public List<MemberResponse> toResponseList(List<Member> members) {
        if (members == null) {
            return null;
        }
        return members.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Member toEntity(MemberRequest request) {
        if (request == null) {
            return null;
        }

        return Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .specialization(request.getSpecialization())
                .role(request.getRole() != null ? request.getRole() : MemberRole.DOCTORANT)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
    }

    public void updateEntity(Member member, MemberRequest request) {
        if (member == null || request == null) {
            return;
        }

        if (request.getFirstName() != null) {
            member.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            member.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            member.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            member.setPassword(request.getPassword());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getDepartment() != null) {
            member.setDepartment(request.getDepartment());
        }
        if (request.getSpecialization() != null) {
            member.setSpecialization(request.getSpecialization());
        }
        if (request.getRole() != null) {
            member.setRole(request.getRole());
        }
        if (request.getActive() != null) {
            member.setActive(request.getActive());
        }
    }
}
