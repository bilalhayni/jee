package com.example.labomasi.enums;

public enum MemberRole {
    ADMIN("Administrator"),
    DIRECTOR("Lab Director"),
    TEACHER("Teacher"),
    DOCTORANT("PhD Student");

    private final String displayName;

    MemberRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}