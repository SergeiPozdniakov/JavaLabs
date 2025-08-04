package com.example.books;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static java.lang.String.format;

@RestController
public class RestControllerBooks {

    @RequestMapping ("/books")
    public String books () {
        return BookStorage.getBooks().
                stream().
                map(book -> format("%s - %s - %s", book.getTitle(), book.getName(), book.getPages())).
                collect(Collectors.joining("</br>"));

    }

}
