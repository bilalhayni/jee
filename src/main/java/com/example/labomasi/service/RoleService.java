package com.example.labomasi.service;

import com.example.labomasi.enums.MemberRole;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {

    public List<MemberRole> findAll() {
        return Arrays.asList(MemberRole.values());
    }

    public MemberRole findByName(String name) {
        return MemberRole.valueOf(name.toUpperCase());
    }

    public boolean exists(String name) {
        try {
            MemberRole.valueOf(name.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
