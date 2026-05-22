import de.easyscoot.model.Customer;
import de.easyscoot.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountServiceTest {

        @Test
        void testCreateAccountSuccess() {

            Customer customer = new Customer(
                    "Max",
                    "Mustermann",
                    "Teststrasse",
                    12,
                    "Hannover",
                    12345,
                    "Max@gmail.com"
            );

            AccountService service = new AccountService(customer);

            assertDoesNotThrow(() -> service.createAccount(customer));
        }

    @Test
    void testCreateAccountNoSuccess() {

        Customer customer = new Customer(
                "Max",
                "Mustermann",
                "Teststrasse",
                12,
                "Hannover",
                12345,
                "Max@gmail.com"
        );

        AccountService service = new AccountService(customer);

        assertThrows(RuntimeException.class, () -> service.createAccount(customer));
    }
}

