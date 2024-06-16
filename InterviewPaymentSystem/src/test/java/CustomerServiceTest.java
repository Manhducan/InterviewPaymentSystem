package test.java;

import main.java.exception.BillNotFoundException;
import main.java.exception.InsufficientFundsException;
import main.java.model.Bill;
import main.java.model.Customer;
import main.java.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        Customer customer = new Customer();
        customerService = new CustomerService(customer);
    }

    @Test
    public void testCashIn() {
        customerService.cashIn(1000000);
        assertEquals(1000000, customerService.getBalance());
    }

    @Test
    public void testPayBill() {
        Bill bill = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        customerService.addBill(bill);
        customerService.cashIn(200000);
        assertDoesNotThrow(() -> customerService.payBill(1));
        assertEquals(0, customerService.getBalance());
        assertEquals("PAID", customerService.listBills().get(0).getState());
    }

    @Test
    public void testPayBillInsufficientFunds() {
        Bill bill = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        customerService.addBill(bill);
        customerService.cashIn(100000);
        assertThrows(InsufficientFundsException.class, () -> customerService.payBill(1));
    }

    @Test
    public void testPayNonExistingBill() {
        assertThrows(BillNotFoundException.class, () -> customerService.payBill(1));
    }

    @Test
    public void testListBills() {
        Bill bill1 = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        Bill bill2 = new Bill(2, "WATER", 150000, LocalDate.now().plusDays(5), "NOT_PAID", "SAVACO HCMC");
        customerService.addBill(bill1);
        customerService.addBill(bill2);
        assertEquals(2, customerService.listBills().size());
    }

    @Test
    public void testAddBill() {
        Bill bill = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        customerService.addBill(bill);
        assertEquals(1, customerService.listBills().size());
    }

    @Test
    public void testDeleteBill() {
        Bill bill = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        customerService.addBill(bill);
        customerService.deleteBill(1);
        assertTrue(customerService.listBills().isEmpty());
    }
    @Test
    public void testSearchBillsByProvider() {
        Bill bill1 = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        Bill bill2 = new Bill(2, "WATER", 150000, LocalDate.now().plusDays(5), "NOT_PAID", "SAVACO HCMC");
        customerService.addBill(bill1);
        customerService.addBill(bill2);
        List<Bill> fetchedBills = customerService.searchBillsByProvider("EVN HCMC");
        assertEquals(1, fetchedBills.size());
        assertEquals("ELECTRIC", fetchedBills.get(0).getType());
    }

    @Test
    public void testListDueBills() {
        Bill bill1 = new Bill(1, "ELECTRIC", 200000, LocalDate.now().minusDays(1), "NOT_PAID", "EVN HCMC");
        Bill bill2 = new Bill(2, "WATER", 150000, LocalDate.now().plusDays(5), "NOT_PAID", "SAVACO HCMC");
        customerService.addBill(bill1);
        customerService.addBill(bill2);
        List<Bill> dueBills = customerService.listDueBills();
        assertEquals(1, dueBills.size());
        assertEquals("WATER", dueBills.get(0).getType());
    }

    @Test
    public void testSearchBillById() {
        Bill bill = new Bill(1, "ELECTRIC", 200000, LocalDate.now(), "NOT_PAID", "EVN HCMC");
        customerService.addBill(bill);
        Bill fetchedBill = customerService.searchBillById(1);
        assertNotNull(fetchedBill);
        assertEquals(1, fetchedBill.getId());
    }
}
