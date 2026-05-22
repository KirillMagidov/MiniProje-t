package de.easyscoot.service;

import de.easyscoot.model.EScooter;
import de.easyscoot.repository.IScooterRepository;

import java.util.List;


public class EScooterService implements IFlottenmanagerService {

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
}
