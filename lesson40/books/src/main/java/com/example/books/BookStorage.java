package com.example.books;

import java.util.ArrayList;
import java.util.List;

public class BookStorage {

    private static final List<ModelBooks> books = new ArrayList<>();

    public static List<ModelBooks> getBooks() {
       return books;
    }

}
