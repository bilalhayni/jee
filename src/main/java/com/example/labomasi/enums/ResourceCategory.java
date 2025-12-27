package com.example.labomasi.enums;

public enum ResourceCategory {
    EQUIPMENT("Equipment"),
    SOFTWARE("Software"),
    MATERIAL("Material"),
    ROOM("Room"),
    OTHER("Other");

    private final String displayName;

    ResourceCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}