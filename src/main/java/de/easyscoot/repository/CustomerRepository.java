package de.easyscoot.repository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.easyscoot.model.Customer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {

    private final String filePath = "src/main/java/de/easyscoot/Database/Customer.json";
    private final Gson gson = new Gson();

    // Alle Kunden in der Datei laden
    public List<Customer> getAllCustomers() {
        try {
            FileReader reader = new FileReader(filePath);

            Type listType = new TypeToken<List<Customer>>(){}.getType(); //ListenTyp für gson
            List<Customer> customers = gson.fromJson(reader, listType);

            if (customers == null) { //wenn Liste leer und gson null gibt
                return new ArrayList<>();
            }

            return customers;

        } catch (Exception e) { //falls Probleme mit der Datei, exception geworfen
            e.printStackTrace();
            throw new RuntimeException("Customer.json nicht gefunden", e);
        }
    }

    // neuen Kunden speichern
    public void saveCustomer(Customer customer) {
        List<Customer> customers = getAllCustomers();
        customers.add(customer);

        try {
            FileWriter writer = new FileWriter(filePath);
            gson.toJson(customers, writer); //schreibt customer in die Datei
            writer.close(); //schließt die Datei
        } catch (Exception e) {
            e.printStackTrace(); //Falls fehler auftritt wird es angezeigt
        }
    }

    // prüft ob Email existiert
    public boolean emailExists(String email) {
        List<Customer> customers = getAllCustomers();

        //iteriert durch die list mit den Customer
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}

//jdbc datenbank postres