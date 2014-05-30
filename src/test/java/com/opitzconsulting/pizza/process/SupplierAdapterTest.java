package com.opitzconsulting.pizza.process;

import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class SupplierAdapterTest {

    private SupplierAdapter supplierAdapter;

    @Mock
    private BusinessProcess businessProcess;

    @Before
    public void setUp() {
        supplierAdapter = new SupplierAdapter();
        supplierAdapter.businessProcess = businessProcess;
    }

    @Test
    public void sendOrderSetsSupplierCorrelationId() {
        supplierAdapter.sendOrder();

        verify(businessProcess).setVariable(eq(SupplierAdapter.SUPPLIER_CORRELATION_ID), anyString());
    }

}
