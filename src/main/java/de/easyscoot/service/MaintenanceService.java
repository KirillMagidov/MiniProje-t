package de.easyscoot.service;

import de.easyscoot.model.EScooter;
import de.easyscoot.model.Maintenancestatus;
import de.easyscoot.repository.IScooterRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService implements IServiceArbeiterService {

    private IScooterRepository scooterRepository;

    public MaintenanceService(IScooterRepository scooterRepository) {
        this.scooterRepository = scooterRepository;
    }

    @Override
    public List<EScooter> getScootersLowBattery(double thresholdPercentage) {
        ArrayList<EScooter> scootersLowBattery = new ArrayList<>();
        scooterRepository.findAll().forEach(scooter -> {
            if(scooter.getLadezustand() <= thresholdPercentage) {
                scootersLowBattery.add(scooter);
            }

        });
        return scootersLowBattery;
    }

    @Override
    public void updateMaintenanceMode(String id, Maintenancestatus status) {
        EScooter scooter = scooterRepository.findById(id);

        if (scooter != null) {
            scooter.setStatus(status);
            scooterRepository.save(scooter);
        } else {
            throw new RuntimeException("Scooter not found");
        }

    }

    @Override
    public void addScooter(EScooter scooter) {
        scooterRepository.save(scooter);
    }

    @Override
    public void removeScooter(String scooterId) {
        scooterRepository.delete(scooterId);
    }

    @Override
    public List<EScooter> getAllEScooter() {
        return scooterRepository.findAll();
    }

    @Override
    public EScooter getScooterDetails(String id) {
        return scooterRepository.findById(id);
    }
}
