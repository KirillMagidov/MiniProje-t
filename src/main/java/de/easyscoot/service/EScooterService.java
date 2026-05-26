package de.easyscoot.service;

import de.easyscoot.model.Drivestatus;
import de.easyscoot.model.EScooter;
import de.easyscoot.repository.IScooterRepository;

import java.util.List;


public class EScooterService implements IFlottenmanagerService, ITelemetrieReceiver {

    private final IScooterRepository scooterRepository;

    public EScooterService(IScooterRepository scooterRepository) {
        this.scooterRepository = scooterRepository;
    }

    @Override
    public List<EScooter> getAllScooters() {
        return scooterRepository.findAll();
    }

    @Override
    public EScooter getScooterDetails(String id) {
        return scooterRepository.findById(id);
    }

    @Override
    public void receiveTelemetrie(String scooterId, String position, double ladezustand, Drivestatus drivestatus){
        EScooter scooter = scooterRepository.findById(scooterId);
        if (scooter != null) {
            scooter.updateZustand(position,ladezustand,drivestatus);
            scooterRepository.save(scooter);
        }
    }
}
