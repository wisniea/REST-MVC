package pl.wisniea.restmvc_data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.wisniea.restmvc_data.entities.BookEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity, Long> {


    BookEntity findByTitle(String title);

    Optional<BookEntity> findById(Long bookId);

    @Query("from books b join b.authors")
    List<BookEntity> findAll();

    List<BookEntity> findAllByTitleContainingIgnoreCase(String title);


}
