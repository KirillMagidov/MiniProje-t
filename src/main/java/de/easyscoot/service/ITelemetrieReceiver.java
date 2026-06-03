package de.easyscoot.service;

import de.easyscoot.model.Drivestatus;

public interface ITelemetrieReceiver {

    void receiveTelemetrie(String scooterId, double latitude, double longitude, double ladezustand, Drivestatus drivestatus);

}