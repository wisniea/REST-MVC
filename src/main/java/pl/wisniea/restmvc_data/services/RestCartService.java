package pl.wisniea.restmvc_data.services;

import pl.wisniea.restmvc_data.entities.BookEntity;
import pl.wisniea.restmvc_data.entities.RestCartEntity;
import pl.wisniea.restmvc_data.entities.UserEntity;

public interface RestCartService {

    RestCartEntity getCartByUser(UserEntity user);

    BookEntity addToCart(UserEntity user, BookEntity book);

    RestCartEntity save(RestCartEntity cart);


}
