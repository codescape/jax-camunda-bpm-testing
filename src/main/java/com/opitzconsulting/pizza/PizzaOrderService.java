package com.opitzconsulting.pizza;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.BusinessProcess;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;

@Stateless
public class PizzaOrderService {

    public static final String PIZZA_ORDER_PROCESS_KEY = "pizza";

    @Inject
    private RuntimeService runtimeService;

    @Inject
    private BusinessProcess businessProcess;

    @PersistenceContext
    private EntityManager entityManager;

    public long processNewOrder(Order newOrder) {
        entityManager.persist(newOrder);

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("orderId", newOrder.getId());

        runtimeService.startProcessInstanceByKey(PIZZA_ORDER_PROCESS_KEY, variables);

        return newOrder.getId();
    }

    @Produces
    @Named("order")
    public Order produceOrderForProcessInstance() {
        long orderId = businessProcess.getVariable("orderId");
        return findOrderById(orderId);
    }

    public Order findOrderById(long orderId) {
        return entityManager.find(Order.class, orderId);
    }

}
