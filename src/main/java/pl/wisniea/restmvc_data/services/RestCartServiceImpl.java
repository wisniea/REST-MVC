package pl.wisniea.restmvc_data.services;

import org.springframework.stereotype.Service;
import pl.wisniea.restmvc_data.repositories.RestCartRepository;
import pl.wisniea.restmvc_data.entities.BookEntity;
import pl.wisniea.restmvc_data.entities.RestCartEntity;
import pl.wisniea.restmvc_data.entities.UserEntity;

@Service
public class RestCartServiceImpl implements RestCartService {

    private final RestCartRepository restCartRepository;

    public RestCartServiceImpl(RestCartRepository restCartRepository) {
        this.restCartRepository = restCartRepository;
    }


    @Override
    public RestCartEntity getCartByUser(UserEntity user) {

        return restCartRepository.findByUser(user);
    }

    @Override
    public BookEntity addToCart(UserEntity user, BookEntity book) {

        RestCartEntity cart = user.getCart();

        cart.getBooks().add(book);

        restCartRepository.save(cart);

        return book;
    }

    @Override
    public RestCartEntity save(RestCartEntity cart) {
        return restCartRepository.save(cart);
    }
}
