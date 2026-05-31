package de.easyscoot.service;

import de.easyscoot.model.Drivestatus;
import de.easyscoot.model.EScooter;
import de.easyscoot.repository.IScooterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public void receiveTelemetrie(String scooterId, double latitude, double longitude, double ladezustand, Drivestatus drivestatus){
        EScooter scooter = scooterRepository.findById(scooterId);
        if (scooter != null) {
            scooter.updateZustand(latitude, longitude ,ladezustand,drivestatus);
            scooterRepository.save(scooter);
        }
    }
}
