package pl.wisniea.restmvc_web.mvccontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.wisniea.restmvc_data.exceptions.UserServiceException;
import pl.wisniea.restmvc_data.entities.BookEntity;
import pl.wisniea.restmvc_data.entities.OrderEntity;
import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.request.OrderRequest;
import pl.wisniea.restmvc_data.services.BookService;
import pl.wisniea.restmvc_data.services.OrderService;
import pl.wisniea.restmvc_data.services.RestCartService;
import pl.wisniea.restmvc_data.services.UserService;
import pl.wisniea.restmvc_web.restcontrollers.CartRestController;

import java.security.Principal;
import java.util.Set;

@Controller
@SessionAttributes("order")
@AllArgsConstructor
public class OrderMvcController {

    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;
    private final CartRestController cart;

    @ModelAttribute("order")
    public OrderRequest getOrder() {
        // session attribute
        return new OrderRequest();
    }

    @PostMapping("/order/add/{bookId}")
    public String addBookToOrder(@ModelAttribute("order") OrderRequest order, @PathVariable Long bookId) {

        order.getBooks().add(bookService.findByBookId(bookId));


        return "index";
    }

    @GetMapping("/order")
    public String showOrder(@ModelAttribute("order") OrderRequest order, Model model) {

        model.addAttribute("orderlist", order.getBooks());
        model.addAttribute("order", order);

        return "orderview";
    }

    @PostMapping("/order/confirm")
    public String confirmOrder(@ModelAttribute("order") OrderRequest orderRequest, Principal principal, Model model, SessionStatus status) throws UserServiceException, JsonProcessingException {
        Set<BookEntity> booksRequest = orderRequest.getBooks();

        OrderEntity order = new OrderEntity();
        UserEntity user = userService.getUserByEmail(principal.getName());
        order.setBooks(booksRequest);
        order.setUser(user);
        orderService.saveOrder(order);
        cart.confirmCartAndPlaceOrder(principal);

        status.setComplete();

        model.addAttribute("order", new OrderRequest());

        return "index";
    }
}
