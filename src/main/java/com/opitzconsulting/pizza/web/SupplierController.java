package com.opitzconsulting.pizza.web;


import com.opitzconsulting.pizza.process.SupplierAdapter;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class SupplierController implements Serializable {

    @EJB
    private SupplierAdapter supplierAdapter;

    @Setter
    @Getter
    private String correlationId;

    @Setter
    @Getter
    private String comment;

    public void submit() {
        supplierAdapter.orderConfirmationReceived(correlationId, comment);
    }

}
