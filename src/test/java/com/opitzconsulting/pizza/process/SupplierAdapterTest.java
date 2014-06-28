package com.opitzconsulting.pizza.process;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SupplierAdapterTest {

    private SupplierAdapter supplierAdapter;

    @Mock
    private BusinessProcess businessProcess;

    @Mock
    private RuntimeService runtimeService;

    @Before
    public void setUp() {
        supplierAdapter = new SupplierAdapter();
        supplierAdapter.businessProcess = businessProcess;
        supplierAdapter.runtimeService = runtimeService;
    }

    @Test
    public void sendOrderSetsSupplierCorrelationId() {
        supplierAdapter.sendOrder();

        verify(businessProcess).setVariable(eq(SupplierAdapter.SUPPLIER_CORRELATION_ID), anyString());
    }

}
