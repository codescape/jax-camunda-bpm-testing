package com.opitzconsulting.pizza.web;

import com.opitzconsulting.pizza.Order;
import com.opitzconsulting.pizza.PizzaOrderService;
import lombok.Getter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class PizzaOrderController implements Serializable {

    @Getter
    private Order newOrder = new Order();

    @Inject
    private PizzaOrderService pizzaOrderService;

    public void submitOrder() {
        pizzaOrderService.processNewOrder(newOrder);
        newOrder = new Order();
    }

}
