import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;
import de.easyscoot.service.AccountService;
import de.easyscoot.service.ValidationService;
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
        Customer customer = new Customer("Max", "Mustermann", "Straße", 23, "Bremen", 2323, "Test@gmail.com", "ahhhhhhh123A2");
        repo.saveCustomer(customer);

        List<Customer> customers = repo.getAllCustomers();
        assertFalse(customers.isEmpty()); // Liste muss mindestens 1 Kunde haben
    }

    @Test
    void testEmailExists() {
        boolean exists = repo.emailExists("mail@mail.de");
        assertFalse(exists); // Email darf nicht gefunden werden
    }

    //funkioniert nun auch
    @Test
    void testRemoveCustomer() {
        Customer c = new Customer("Max", "Mustermann", "Straße", 23, "Bremen", 2323, "furalles56@gmail.com","ahhhhhhh123A2");
        ValidationService vService = new ValidationService();
        AccountService service = new AccountService(repo, vService);
        service.createAccount(c);
        repo.removeCustomer(c.getCustomerId());
        boolean exists = repo.emailExists("Test@gmail.com");
        assertFalse(exists);
    }


    //funktioniert bereits
    @Test
    void testChangeCustomerData () {
        Customer c = new Customer("Max", "Mustermann", "Straße", 23, "Bremen", 2323, "furalles56@gmail.com","ahhhhhhh123A2");
        ValidationService vService = new ValidationService();
        AccountService service = new AccountService(repo, vService);
        service.createAccount(c);
        Customer newCustomer = new Customer ("Dave", "Mustermann", "Straße", 23, "Bremen", 2323, "Test@gmail.com", "ahhhhhhh123A2");
        repo.changeCustomerData (c.getCustomerId(), newCustomer);
    }

}
