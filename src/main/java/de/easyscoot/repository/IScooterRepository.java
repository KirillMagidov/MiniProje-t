package de.easyscoot.repository;

import de.easyscoot.model.EScooter;

import java.util.List;

public interface IScooterRepository {
    //Liste alle gespeicherten Escooter
    List<EScooter> findAll();

    //Escooter mit ID finden
    EScooter findById(String Id);

    //Scooter wird im System gespeichert
    void save(EScooter scooter);

    //Scooter wird aus System gelöscht
    void delete(String Id);
}
