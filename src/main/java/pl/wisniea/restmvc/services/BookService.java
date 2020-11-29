package pl.wisniea.restmvc.services;

import pl.wisniea.restmvc.domain.Book;

import java.util.List;

public interface BookService {
    Book findBookById(Long id);
    List<Book> findAllBooks();
    Book saveBook(Book book);
}
