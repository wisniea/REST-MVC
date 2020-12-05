package pl.wisniea.restmvc_data.response;

import lombok.Data;
import pl.wisniea.restmvc_data.entities.BookEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartRest {

    private String cartId;
    private UserRest userRest;
    private List<BookEntity> books = new ArrayList<>();
}
