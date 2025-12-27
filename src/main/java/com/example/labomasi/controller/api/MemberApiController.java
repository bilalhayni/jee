package com.example.labomasi.controller.api;

import com.example.labomasi.dto.request.MemberRequest;
import com.example.labomasi.dto.response.MemberResponse;
import com.example.labomasi.entity.Member;
import com.example.labomasi.exception.BadRequestException;
import com.example.labomasi.exception.ResourceNotFoundException;
import com.example.labomasi.mapper.MemberMapper;
import com.example.labomasi.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberApiController(MemberService memberService, MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(memberMapper.toResponseList(members));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
        return ResponseEntity.ok(memberMapper.toResponse(member));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberResponse>> searchMembers(@RequestParam String query) {
        List<Member> members = memberService.search(query);
        return ResponseEntity.ok(memberMapper.toResponseList(members));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request) {
        if (memberService.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        Member member = memberMapper.toEntity(request);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        Member savedMember = memberService.save(member);

        return new ResponseEntity<>(memberMapper.toResponse(savedMember), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {

        Member member = memberService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));

        if (!member.getEmail().equals(request.getEmail()) && memberService.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        memberMapper.updateEntity(member, request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Member updatedMember = memberService.save(member);
        return ResponseEntity.ok(memberMapper.toResponse(updatedMember));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (memberService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Member", id);
        }
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
