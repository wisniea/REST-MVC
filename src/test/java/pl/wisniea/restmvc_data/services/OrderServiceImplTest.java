package pl.wisniea.restmvc_data.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.wisniea.restmvc_data.exceptions.BookServiceException;
import pl.wisniea.restmvc_data.entities.BookEntity;
import pl.wisniea.restmvc_data.entities.OrderEntity;
import pl.wisniea.restmvc_data.request.OrderRequest;
import pl.wisniea.restmvc_data.repositories.OrderRepository;
import pl.wisniea.restmvc_data.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static pl.wisniea.restmvc_data.services.BookServiceImplTest.testIsbn;
import static pl.wisniea.restmvc_data.services.BookServiceImplTest.testTitle;

class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepository;
    @Mock
    Utils utils;

    OrderEntity orderEntity;
    OrderEntity orderEntity02;
    OrderRequest orderRequest;

    BookEntity bookEntity;
    BookEntity bookEntity02;
    List<OrderEntity> listOfOrders;

    static final String orderId = "S0m4orrdrId133";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        orderEntity = new OrderEntity();
        orderRequest = new OrderRequest();

        bookEntity = new BookEntity();
        bookEntity.setTitle(testTitle);
        bookEntity.setIsbn(testIsbn);

        bookEntity02 = new BookEntity();
        bookEntity02.setTitle("Test book 02");
        bookEntity02.setIsbn(testIsbn);

        orderEntity = new OrderEntity();
        orderEntity02 = new OrderEntity();

        orderEntity.setBooks(Set.of(bookEntity,bookEntity02));
        orderEntity02.setBooks(Set.of(bookEntity,bookEntity02));

        listOfOrders = new ArrayList<>();

    }

    @Test
    void saveOrder() {
        when(utils.generateOrderId(anyInt())).thenReturn(orderId);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderEntity order = orderService.saveOrder(orderEntity);

        assertNotNull(order);
        assertEquals(2, order.getBooks().size());
        verify(utils, times(1)).generateOrderId(anyInt());
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void getOrderById() throws BookServiceException {

        when(orderRepository.findByOrderId(orderId)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        OrderEntity order = orderService.getOrderById(orderId);

        assertThat(orderRepository.findByOrderId(orderId).isPresent());
        verify(orderRepository,times(2)).findByOrderId(orderId);
        assertNotNull(order);
        assertNotNull(order.getBooks());


    }

    @Test
    void getAllOrders() {
        when(orderRepository.findAll()).thenReturn(listOfOrders);

        List<OrderEntity> allOrders = orderService.getAllOrders();

        assertNotNull(allOrders);
        verify(orderRepository,times(1)).findAll();

    }
}