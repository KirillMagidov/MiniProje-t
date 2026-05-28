package de.easyscoot.model;

public class EScooter {

    private Maintenancestatus status;
    private double ladezustand;
    private String id;
    private String marke;
    private String modell;
    private double batteriekapazitaet;
    private double verbrauchskoeffizient;
    private double latitude;
    private double longitude;
    private Drivestatus drivestatus;
    private Availability availability;

    public EScooter() {

    }

    public EScooter(Maintenancestatus status, String id, String marke, String modell, double latitude, double longitude
            , double ladezustand, double batteriekapazitaet, double verbrauchskoeffizient, Drivestatus drivestatus,
                    Availability availability) {
        this.status = status;
        this.id = id;
        this.marke = marke;
        this.modell = modell;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ladezustand = ladezustand;
        this.batteriekapazitaet = batteriekapazitaet;
        this.verbrauchskoeffizient = verbrauchskoeffizient;
        this.drivestatus = drivestatus;
        this.availability = availability;
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public void updateZustand(double latitude, double longitude, double lade, Drivestatus drivestatus) {
        this.ladezustand = lade;
        this.latitude = latitude;
        this.longitude = longitude;
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
