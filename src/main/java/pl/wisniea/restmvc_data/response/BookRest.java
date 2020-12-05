package pl.wisniea.restmvc_data.response;

import lombok.Data;
import pl.wisniea.restmvc_data.entities.AuthorEntity;

import java.util.HashSet;
import java.util.Set;

@Data
public class BookRest {

    private Long id;
    private String title;
    private String isbn;
    private Set<AuthorEntity> authors = new HashSet<>();

    public BookRest() {
    }

    public BookRest(Long id, String title) {
        this.id = id;
        this.title = title;
    }


}
