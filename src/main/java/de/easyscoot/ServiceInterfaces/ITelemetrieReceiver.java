package de.easyscoot.ServiceInterfaces;

import de.easyscoot.model.Drivestatus;

public interface ITelemetrieReceiver {
    //
    void receiveTelemetrie(String scooterId, String pos, double batteryStatus, Drivestatus fahrstatus);
}
