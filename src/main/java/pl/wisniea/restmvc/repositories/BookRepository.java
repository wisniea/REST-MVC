package pl.wisniea.restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wisniea.restmvc.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
