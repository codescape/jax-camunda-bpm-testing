package com.opitzconsulting.pizza.process;

import com.opitzconsulting.pizza.Order;
import lombok.extern.java.Log;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import java.util.UUID;
import java.util.logging.Logger;

@Log
@Named("supplierAdapter")
@Stateless
@WebService
public class SupplierAdapter {

    public static final String SUPPLIER_CORRELATION_ID = "SUPPLIER_CORRELATION_ID";

    @Inject
    BusinessProcess businessProcess;

    @Inject
    RuntimeService runtimeService;

    @Inject
    @Named
    private Order order;

    public void sendOrder() {
        log.info("Sende Bestellung an lokalen Dienstleister...");
        String correlationId = UUID.randomUUID().toString();
        businessProcess.setVariable(SUPPLIER_CORRELATION_ID, correlationId);

        log.info("Sendungsnummer " + correlationId);
    }

    public void orderConfirmationReceived(String correlationId, String somePayload) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().variableValueEquals(SUPPLIER_CORRELATION_ID, correlationId).singleResult();
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).messageEventSubscriptionName("CONFIRMATION").singleResult();

        runtimeService.setVariable(execution.getId(), "supplierResponsePayload", somePayload);
        runtimeService.messageEventReceived("CONFIRMATION", execution.getId());
    }

}
