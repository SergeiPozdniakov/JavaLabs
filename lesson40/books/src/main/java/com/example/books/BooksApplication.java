package com.example.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksApplication {

	public static void main(String[] args) {

		BookStorage.getBooks().add(
				new ModelBooks("Star Wars", "Djedy", 1000)
				);
				BookStorage.getBooks().add(
				new ModelBooks("War and Peace", "Tolstoy", 1200)
				);
		SpringApplication.run(BooksApplication.class, args);
	}

}
