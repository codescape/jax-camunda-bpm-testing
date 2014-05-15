package com.opitzconsulting.pizza.process;

import com.opitzconsulting.pizza.Order;
import lombok.extern.java.Log;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javax.inject.Inject;
import javax.inject.Named;

@Log
@Named("emailAdapter")
public class EmailAdapter {

    @Inject
    @Named
    private Order order;

    public void sendConfirmation() throws EmailException {
        log.info("Sende Bestätigungsmail...");

        if (order.getWeatherInfo() != null) {
            sendMail(order.getEmail(),
                    "Auftragsbestätigung #" + order.getId(),
                    "Deine Bestellung ist unterwegs. Temperatur in " + order.getCity() + ": " + order.getWeatherInfo()
            );
        } else {
            sendMail(order.getEmail(), "Auftragsbestätigung #" + order.getId(), "Deine Bestellung ist unterwegs.");
        }
    }

    public void sendRejection() throws EmailException {
        log.info("Sende Ablehnungsmail...");
        sendMail(order.getEmail(), "Ablehnungsinformation #" + order.getId(), "Sorry, das klappt leider nicht.");
    }

    private void sendMail(String target, String subject, String body) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(25);
        email.setFrom("noreply@pizza-order-demo.de");
        email.setSubject(subject);
        email.setMsg(body);
        email.addTo(target);
        email.send();
    }

}
