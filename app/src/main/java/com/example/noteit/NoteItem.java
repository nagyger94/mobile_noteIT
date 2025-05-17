package com.example.noteit;

public class NoteItem {
    private String title;
    private String description;

    public NoteItem(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
