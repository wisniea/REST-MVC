package pl.wisniea.restmvc_data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.wisniea.restmvc_data.entities.AuthorEntity;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {

    List<AuthorEntity> findAllByFirstNameOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("select a from authors a where concat(lower(a.firstName) , ' ' , lower( a.lastName)) like lower(concat('%', :author,'%'))  ")
    List<AuthorEntity> findAllByAuthorFullName(String author);
}
