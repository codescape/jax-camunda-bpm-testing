jax-camunda-bpm-testing
=======================

Source Code zum Vortrag "BPM - It’s done when it is tested!" auf der JAX 2014.

Agiles Vorgehen macht vor keinem Projekt halt, erst recht nicht vor BPM-Projekten. Wie können wir mit einer performanten Testautomatisierung die Funktionalität und Wartbarkeit unserer automatisierten Prozesse sicherstellen? Wie lassen sich die bekannten und liebgewonnen Testmechanismen aus der Java-Welt auf die Prozessdenkweise übertragen? Anhand von Live-Beispielen auf der camunda BPM Plattform und der Oracle BPM Suite erläutern wir praxiserprobte Testarchitekturen und -lösungen.

Vorbereitung
------------

* SMTP Server auf localhost:25 gestartet (bspw. https://github.com/Nilhcem/FakeSMTP)
* camunda BPM 7.1.0 Final auf localhost:8080 gestartet

Durchführung
------------

### Unit Testing

* src/test/java/com/opitzconsulting/pizza/unit/PizzaProcessTest.java

### Integration Testing

* src/test/java/com/opitzconsulting/pizza/ArquillianPizzaProcessTest.java
