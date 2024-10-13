package org.example;

import java.util.List;

public class BookTest {
    private final String title;
    private final String author;
    private final int pages;
    private final List<String> genres;
    private final String[] tags;
    private boolean read;

    public BookTest(String title, String author, int pages, List<String> genres, String[] tags, boolean read) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.genres = genres;
        this.tags = tags;
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String[] getTags() {
        return tags;
    }

    public boolean isRead() {
        return read;
    }
}
