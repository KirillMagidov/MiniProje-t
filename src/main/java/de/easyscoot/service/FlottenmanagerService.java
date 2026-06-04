package de.easyscoot.service;

import de.easyscoot.model.EScooter;
import de.easyscoot.repository.IScooterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlottenmanagerService implements IFlottenmanagerService {

    private final IScooterRepository scooterRepository;

    public FlottenmanagerService(IScooterRepository scooterRepository) {
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
}
