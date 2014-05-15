package com.opitzconsulting.pizza;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ORDER_OOP")
public class Order {

    @Id
    @GeneratedValue
    private long id;

    private String email;

    private String zip;
    private String city;

    private String orderDetails;
    private long amount;

    private String weatherInfo;

}
