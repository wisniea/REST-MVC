package pl.wisniea.restmvc_data.services;


import pl.wisniea.restmvc_data.exceptions.BookServiceException;
import pl.wisniea.restmvc_data.entities.OrderEntity;

import java.util.List;

public interface OrderService {

    OrderEntity saveOrder(OrderEntity order);

    OrderEntity getOrderById(String id) throws BookServiceException;

    List<OrderEntity> getAllOrders();
}
