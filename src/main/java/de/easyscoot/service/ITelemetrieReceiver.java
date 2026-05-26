package de.easyscoot.service;

import de.easyscoot.model.Drivestatus;

public interface ITelemetrieReceiver {
    void receiveTelemetrie(String scooterId, String position, double ladezustand, Drivestatus drivestatus);
}