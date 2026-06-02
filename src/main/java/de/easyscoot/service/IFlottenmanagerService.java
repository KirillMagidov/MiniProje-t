package de.easyscoot.service;

import de.easyscoot.model.EScooter;
import java.util.List;

public interface IFlottenmanagerService {

    List<EScooter> getAllScooters();

    EScooter getScooterDetails(String id);

}