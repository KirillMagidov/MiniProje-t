import de.easyscoot.model.Customer;
import de.easyscoot.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AccountServiceTest {

    private AccountService service;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "Max",
                "Mustermann",
                "max@test.de",
                12,
                "Hannover",
                12345,
                "Max@gmail.com"
        );
        service = new AccountService(customer);
    }

    @Test
    void testCreateAccountSuccess() {
        assertDoesNotThrow(() -> service.createAccount(customer));
    }
}
