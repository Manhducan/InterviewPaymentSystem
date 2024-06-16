package main.java.service;

import main.java.exception.BillNotFoundException;
import main.java.exception.InsufficientFundsException;
import main.java.model.Bill;
import main.java.model.Customer;
import main.java.model.PaymentHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Customer customer;

    private List<PaymentHistory> paymentHistorys = new ArrayList<>();

    public CustomerService(Customer customer) {
        this.customer = customer;
    }

    public int getBalance() {
        return customer.getBalance();
    }

    public void cashIn(int amount) {
        customer.setBalance(customer.getBalance() + amount);
    }

    public void payBill(int billId) throws InsufficientFundsException {
        Bill bill = customer.getBills().stream().filter(b -> b.getId() == billId).findFirst().orElse(null);
        if (bill == null) {
            throw new BillNotFoundException("Sorry! Not found a bill with such id");
        }
        if (customer.getBalance() < bill.getAmount()) {
            throw new InsufficientFundsException("Sorry! Not enough fund to proceed with payment.");
        }
        customer.setBalance(customer.getBalance() - bill.getAmount());
        bill.setState("PAID");
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setBill(bill);
        paymentHistory.setDatePayment(LocalDateTime.now().format(DATE_FORMATTER));
        paymentHistory.setAmount(bill.getAmount());
        paymentHistory.setState("PROCESSED");
        if(paymentHistorys.isEmpty()){
            paymentHistory.setId(1);
        } else {
            paymentHistory.setId(paymentHistorys.size() + 1);
        }
        paymentHistorys.add(paymentHistory);

    }

    public List<Bill> listBills() {
        return customer.getBills();
    }

    public void addBill(Bill bill) {
        customer.getBills().add(bill);
    }

    public void deleteBill(int billId) {
        customer.getBills().removeIf(bill -> bill.getId() == billId);
    }

    public List<PaymentHistory> listPaymentHistory() {
        return paymentHistorys;
    }
    public List<Bill> searchBillsByProvider(String provider) {
        return customer.getBills().stream().filter(bill -> bill.getProvider().equalsIgnoreCase(provider)).collect(Collectors.toList());
    }

    public List<Bill> listDueBills() {
        LocalDate currentDate = LocalDate.now();
        return customer.getBills().stream().filter(bill -> bill.getDueDate().isAfter(currentDate) && bill.getState().equals("NOT_PAID")).collect(Collectors.toList());
    }

    public Bill searchBillById(int id) {
        return customer.getBills().stream().filter(bill -> bill.getId() == id).findFirst().orElse(null);
    }
}
