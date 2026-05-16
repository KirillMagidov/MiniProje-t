package de.easyscoot.service;

import de.easyscoot.model.Drivestatus;

public interface ITelemetrieReceiver {
    //
    void receiveTelemetrie(String scooterId, String pos, double batteryStatus, Drivestatus fahrstatus);
}
