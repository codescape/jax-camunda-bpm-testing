package com.opitzconsulting.pizza.process;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CurrentWeather")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class WeatherResponse {

    @XmlElement(name = "Temperature")
    private String temperature;

}
