import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepositoryTest {

    CustomerRepository repo = new CustomerRepository();

    @Test
    void testGetAllCustomers() {
        CustomerRepository repo = new CustomerRepository();
        List<Customer> customers = repo.getAllCustomers();
        assertNotNull(customers); // Liste darf nie null sein
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer("Max", "Mustermann", "Straße", 23, "Bremen", 2323, "Test@gmail.com");
        repo.saveCustomer(customer);

        List<Customer> customers = repo.getAllCustomers();
        assertFalse(customers.isEmpty()); // Liste muss mindestens 1 Kunde haben
    }

    @Test
    void testEmailExists() {
        boolean exists = repo.emailExists("mail@mail.de");
        assertFalse(exists); // Email darf nicht gefunden werden
    }
}
