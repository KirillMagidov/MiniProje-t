package de.easyscoot.model;

import de.easyscoot.service.ITelemetrieReceiver;

public class EScooter {

    private Maintenancestatus status;
    private double ladezustand;
    private String id;
    private String marke;
    private String modell;
    private double batteriekapazitaet;
    private double verbrauchskoeffizient;
    private String position;
    private Drivestatus drivestatus;
    private Availability availability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drivestatus getDrivestatus() {
        return drivestatus;
    }

    public void setDrivestatus(Drivestatus drivestatus) {
        this.drivestatus = drivestatus;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public double getBatteriekapazitaet() {
        return batteriekapazitaet;
    }

    public void setBatteriekapazitaet(double batteriekapazitaet) {
        this.batteriekapazitaet = batteriekapazitaet;
    }

    public double getVerbrauchskoeffizient() {
        return verbrauchskoeffizient;
    }

    public void setVerbrauchskoeffizient(double verbrauchskoeffizient) {
        this.verbrauchskoeffizient = verbrauchskoeffizient;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public double getLadezustand() {
        return ladezustand;
    }

    public void setLadezustand(double ladezustand) {
        this.ladezustand = ladezustand;
    }

    public Maintenancestatus getStatus() {
        return status;
    }

    public void setStatus(Maintenancestatus status) {
        this.status = status;
    }

    public void updateZustand(String pos, double lade, Drivestatus drivestatus) {
        this.ladezustand = lade;
        this.position = pos;
        this.drivestatus = drivestatus;
    }

    public int getRestfahrzeit() {
        double energie;
        double zeit;

        energie = batteriekapazitaet * (ladezustand / 100);
        zeit = energie / verbrauchskoeffizient;
        return (int) zeit;
    }
}
