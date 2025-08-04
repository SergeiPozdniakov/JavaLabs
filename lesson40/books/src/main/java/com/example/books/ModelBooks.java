package com.example.books;

public class ModelBooks {
    private String Title;
    private String name;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    private int pages;

    public ModelBooks() {};

    public ModelBooks(String title, String name, int pages) {
        Title = title;
        this.name = name;
        this.pages = pages;
    }






}
