package de.easyscoot.repository;

import de.easyscoot.model.EScooter;

import java.util.List;

public class ScooterRepository implements IScooterRepository{

    @Override
    public List<EScooter> findAll() {
        return List.of();
    }

    @Override
    public EScooter findById(String Id) {
        return null;
    }

    @Override
    public void save(EScooter scooter) {

    }

    @Override
    public void delete(String Id) {

    }
}
