package pl.wisniea.restmvc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wisniea.restmvc.domain.Book;
import pl.wisniea.restmvc.services.BookService;

import java.util.List;

@RestController
@RequestMapping(BookController.BASE_URL)
public class BookController {
    public static final String BASE_URL = "/api/v1/books";
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping
    List<Book> getAllBooks(){return bookService.findAllBooks();}
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id){return bookService.findBookById(id);}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book saveBook(Book book){return bookService.saveBook(book);}
}
