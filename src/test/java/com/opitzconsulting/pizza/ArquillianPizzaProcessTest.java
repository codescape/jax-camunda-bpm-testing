package com.opitzconsulting.pizza;


import com.opitzconsulting.pizza.process.SupplierAdapter;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ArquillianPizzaProcessTest {

    private static final String PROCESS_DEFINITION_KEY = "pizza";

    @Deployment
    public static WebArchive createDeployment() {
        PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");

        return ShrinkWrap
                .create(WebArchive.class, "pizza-order-demo.war")
                        // add required libraries
                .addAsLibraries(resolver.resolve("org.camunda.bpm:camunda-engine-cdi").withTransitivity().asFile())
                .addAsLibraries(resolver.resolve("org.camunda.bpm.javaee:camunda-ejb-client").withTransitivity().asFile())
                .addAsLibraries(resolver.resolve("org.apache.commons:commons-email").withTransitivity().asFile())
                .addAsLibraries(resolver.resolve("org.jboss.resteasy:resteasy-jaxrs").withTransitivity().asFile())
                .addAsLibraries(resolver.resolve("org.projectlombok:lombok").withTransitivity().asFile())
                        // add marker files
                .addAsWebResource("META-INF/processes.xml", "WEB-INF/classes/META-INF/processes.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebResource("META-INF/persistence.xml", "WEB-INF/classes/META-INF/persistence.xml")
                        // add required classes / packages
                .addPackages(false, "com.opitzconsulting.pizza", "com.opitzconsulting.pizza.process")
                        // add required wsdl and classes for weather webservice
                .addAsResource("wsdl/globalweather.wsdl")
                .addPackage("net.webservicex")
                        // add process definition and image
                .addAsResource("pizza.bpmn")
                .addAsResource("pizza.png");
    }

    @Inject
    private ProcessEngine processEngine;

    @EJB
    private PizzaOrderService pizzaOrderService;

    @EJB
    private SupplierAdapter supplierAdapter;

    @Before
    public void cleanup() {
        cleanUpRunningProcessInstances();
    }

    @Test
    public void approveHighVolumeOrder() throws Exception {
        Order order = new Order();
        order.setZip("70182");
        order.setEmail("stefan.glase@opitz-consulting.com");
        order.setAmount(300);

        Long orderId = pizzaOrderService.processNewOrder(order);

        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processVariableValueEquals("orderId", orderId).list();
        assertEquals(1, tasks.size());

        Order persistentOrder = pizzaOrderService.findOrderById(orderId);
        assertEquals("Stuttgart", persistentOrder.getCity());

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);
        processEngine.getTaskService().complete(tasks.get(0).getId(), variables);

        String correlationId = (String) processEngine.getRuntimeService().getVariable(tasks.get(0).getProcessInstanceId(), SupplierAdapter.SUPPLIER_CORRELATION_ID);
        assertNotNull(correlationId);
        supplierAdapter.orderConfirmationReceived(correlationId, "Okay, I am shipping!");

        persistentOrder = pizzaOrderService.findOrderById(orderId);
        assertNotNull(persistentOrder.getWeatherInfo());
    }

    private void cleanUpRunningProcessInstances() {
        List<ProcessInstance> runningInstances = processEngine.getRuntimeService()
                .createProcessInstanceQuery().processDefinitionKey(PROCESS_DEFINITION_KEY).list();
        for (ProcessInstance processInstance : runningInstances) {
            processEngine.getRuntimeService().deleteProcessInstance(
                    processInstance.getId(),
                    "cleanup for integration tests"
            );
        }
    }

}
