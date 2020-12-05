package pl.wisniea.restmvc_data.services;

import pl.wisniea.restmvc_data.exceptions.BookServiceException;
import pl.wisniea.restmvc_data.exceptions.UserServiceException;
import pl.wisniea.restmvc_data.entities.BookEntity;
import pl.wisniea.restmvc_data.request.BookRequestModel;
import pl.wisniea.restmvc_data.response.BookRest;

import java.util.List;

public interface BookService {

    BookRest createBook(BookRequestModel bookRequest) throws BookServiceException;

    BookRest getById(Long bookId) throws BookServiceException;

    void deleteBook(Long bookId) throws UserServiceException;

    List<BookEntity> findAll(int page, int limit);

    BookEntity findByBookId(Long bookId);

    List<BookRest> findBooksByTitle(String title);

    List<BookRest> findBooksByAuthor(String author);

}
