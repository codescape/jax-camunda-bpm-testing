package com.opitzconsulting.pizza.unit;

import com.opitzconsulting.pizza.Order;
import com.opitzconsulting.pizza.process.EmailAdapter;
import com.opitzconsulting.pizza.process.GeoAdapter;
import com.opitzconsulting.pizza.process.SupplierAdapter;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verwendung der Konfiguration in der camunda.cfg.xml für die Initialisierung der camunda BPM ProcessEngine auf Basis
 * der In-Memory-Datenbank H2.
 */
public class PizzaProcessTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Mock
    public GeoAdapter geoAdapter;

    @Mock
    public EmailAdapter emailAdapter;

    @Mock
    public SupplierAdapter supplierAdapter;

    @Mock
    public Order order;

    @Before
    public void initializeMocks() {
        MockitoAnnotations.initMocks(this);
        Mocks.register("geoAdapter", geoAdapter);
        Mocks.register("emailAdapter", emailAdapter);
        Mocks.register("supplierAdapter", supplierAdapter);
        Mocks.register("order", order);
    }

    @Test
    @Deployment(resources = "pizza.bpmn")
    public void processDefinitionShouldBeAbleToDeploy() throws Exception {
        // Dieser Test prüft lediglich, ob ein Deployment der Prozessdefinition möglich ist.
    }

    @Test
    @Deployment(resources = "pizza.bpmn")
    public void requireOrderConfirmationForAmountGreaterThan200() throws Exception {
        when(order.getAmount()).thenReturn(300L);

        ProcessInstance pi = runtimeService().startProcessInstanceByKey("pizza");

        assertThat(pi).isStarted().task().isAssignedTo("demo");
    }

    @Test
    @Deployment(resources = "pizza.bpmn")
    public void sendRejectionMailWhenNotApproved() throws Exception {
        when(order.getAmount()).thenReturn(300L);

        ProcessInstance pi = runtimeService().startProcessInstanceByKey("pizza");
        assertThat(pi).isStarted();
        complete(task(), withVariables("approved", false));

        verify(emailAdapter).sendRejection();
        assertThat(pi).isEnded();
    }

    @Test
    @Deployment(resources = "pizza.bpmn")
    public void expectLocalDealerToConfirmOrder() throws Exception {
        when(order.getAmount()).thenReturn(100L);

        ProcessInstance pi = runtimeService().startProcessInstanceByKey("pizza");
        assertThat(pi).isStarted();

        verify(supplierAdapter).sendOrder();
        assertThat(pi).isWaitingAt("SupplierResponseEvent");
    }

}
