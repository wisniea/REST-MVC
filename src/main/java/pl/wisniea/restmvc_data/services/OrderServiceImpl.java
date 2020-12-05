package pl.wisniea.restmvc_data.services;

import org.springframework.stereotype.Service;
import pl.wisniea.restmvc_data.exceptions.BookServiceException;
import pl.wisniea.restmvc_data.entities.OrderEntity;
import pl.wisniea.restmvc_data.repositories.OrderRepository;
import pl.wisniea.restmvc_data.utils.Utils;

import java.util.List;
import java.util.Optional;

import static pl.wisniea.restmvc_data.exceptions.ErrorMessages.NO_RECORD_FOUND;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final Utils utils;

    public OrderServiceImpl(OrderRepository orderRepository, Utils utils) {
        this.orderRepository = orderRepository;
        this.utils = utils;
    }

    @Override
    public OrderEntity saveOrder(OrderEntity order) {
        order.setOrderId(utils.generateOrderId(20));
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity getOrderById(String orderId) throws BookServiceException {
        Optional<OrderEntity> orderEntity = orderRepository.findByOrderId(orderId);
        if (orderEntity.isPresent()) {
            return orderEntity.get();
        } else {
            throw new BookServiceException(NO_RECORD_FOUND.getErrorMessage());
        }
    }

    @Override
    public List<OrderEntity> getAllOrders() {
       return orderRepository.findAll();
    }
}
