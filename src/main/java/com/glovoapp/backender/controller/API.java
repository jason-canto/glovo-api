package com.glovoapp.backender.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glovoapp.backender.dto.OrderVM;
import com.glovoapp.backender.repository.CourierRepository;
import com.glovoapp.backender.repository.OrderRepository;

@RestController
public class API {

    @Value("${backender.welcome_message}")
    private String welcomeMessage;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourierRepository corrierRepository;

    @RequestMapping("/")
    @ResponseBody
    public String root() {
        return welcomeMessage;
    }

    @RequestMapping("/orders")
    @ResponseBody
    public List<OrderVM> orders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderVM(order.getId(), order.getDescription()))
                .collect(Collectors.toList());
    }

    @RequestMapping("/orders/{courierId}")
    @ResponseBody
    public List<OrderVM> getOrdersByCourierById(@PathVariable String courierId) {
        return corrierRepository.findOrdersByCourierById(courierId);
    }

}
