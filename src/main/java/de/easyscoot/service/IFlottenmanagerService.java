package de.easyscoot.service;
import de.easyscoot.model.EScooter;
import java.util.List;


public interface IFlottenmanagerService {

    //List der EScooter einsehen
    List<EScooter> getAllScooters();

    //Details einsehen wie ID, Ladestand, Position usw.
    EScooter getScooterDetails(String id);
}
