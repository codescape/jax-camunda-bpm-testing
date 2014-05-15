package com.opitzconsulting.pizza.process;

import com.opitzconsulting.pizza.Order;
import lombok.extern.java.Log;
import net.webservicex.GlobalWeather;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
@Named("weatherAdapter")
public class WeatherAdapter {

    @Inject
    @Named
    private Order order;

    public void getWeather() {
        log.info("Ermittele Wetter in " + order.getCity());

        try {
            String weatherResponse = new GlobalWeather().getGlobalWeatherSoap().getWeather(order.getCity(), "Germany");
            log.info("\n\n" + weatherResponse + "\n\n");

            WeatherResponse weather = JAXB.unmarshal(new StringReader(weatherResponse), WeatherResponse.class);
            order.setWeatherInfo(weather.getTemperature());

            log.info("Wetter am Zielort emittelt: " + order.getWeatherInfo());
        } catch (Exception e) {
            log.log(Level.WARNING, "Wetter am Zielort konnte nicht ermittelt werden.", e);
        }
    }

}
