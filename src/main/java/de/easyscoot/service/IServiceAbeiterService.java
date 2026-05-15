package de.easyscoot.ServiceInterfaces;
import de.easyscoot.model.EScooter;
import de.easyscoot.model.Maintenancestatus;

import java.util.List;

public interface IServiceAbeiterService {

    //Liste der Escooter bis zum angegebenen Batterlevel einsehen
    List<EScooter> getScootersLowBattery(double thresholdPercentage);

    //Wartungsmodus von Escooter setzen
    void updateMaintenanceMode (String id, Maintenancestatus status);

    void addScooter (EScooter scooter);

    void removeScooter (EScooter scooter);
}




 /*
    void addScooter (EScooter scooter);
    void removeScooter (EScooter scooter)
    noch klären
     */