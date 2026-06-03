package de.easyscoot.service;

import de.easyscoot.model.EScooter;
import de.easyscoot.model.Maintenancestatus;
import java.util.List;

public interface IServiceArbeiterService {

    List<EScooter> getAllEScooter();

    EScooter getScooterDetails(String id);

    List<EScooter> getScootersLowBattery(double thresholdPercentage);

    void updateMaintenanceMode(String id, Maintenancestatus status);

    void addScooter(EScooter scooter);

    void removeScooter(String scooterId);

}