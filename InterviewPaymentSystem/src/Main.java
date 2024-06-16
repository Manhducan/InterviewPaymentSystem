import main.java.exception.InvalidCommandException;
import main.java.model.Bill;
import main.java.model.Customer;
import main.java.model.PaymentHistory;
import main.java.service.CustomerService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
        CustomerService customerService = new CustomerService(customer);
        System.out.println("The program has finished running. Please enter the command you wish to execute!!! (Command list is in the Readme file)");

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0].toUpperCase();

            try {
                switch (command) {
                    case "CASH_IN":
                        int amount = Integer.parseInt(parts[1]);
                        customerService.cashIn(amount);
                        System.out.println("Your available balance: " + customerService.getBalance());
                        break;
                    case "ADD_BILL":
                        int id;
                        while (true) {
                            System.out.print("Input Id: ");
                            id = scanner.nextInt();
                            scanner.nextLine();
                            if (isIdInArray(customerService.listBills(), id)) {
                                System.out.println("ID already exists. Please enter a different ID");
                            } else {
                                break;
                            }
                        }
                        Bill bill = new Bill();
                        bill.setId(id);
                        System.out.print("Input Type: ");
                        bill.setType(scanner.nextLine());
                        System.out.print("Input Amount: ");
                        bill.setAmount(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("Input Due Date: ");
                        bill.setDueDate(LocalDate.parse(scanner.nextLine(), DATE_FORMATTER));
                        System.out.print("Input State: ");
                        bill.setState(scanner.nextLine());
                        System.out.print("Input PROVIDER: ");
                        bill.setProvider(scanner.nextLine());
                        customerService.addBill(bill);
                        System.out.println("Input completed !");
                        scanner.nextLine();
                        break;
                    case "DELETE_BILL":
                        int billIdDelete = Integer.parseInt(parts[1]);
                        customerService.deleteBill(billIdDelete);
                        System.out.println("Delete completed !");
                        break;
                    case "LIST_BILL":
                        listBills(customerService.listBills());
                        break;
                    case "SEARCH_BILL":
                        int billIdSearch = Integer.parseInt(parts[1]);
                        billSearch(customerService.searchBillById(billIdSearch));
                        System.out.println("Delete completed !");
                        break;
                    case "PAY":
                        for (int i = 1; i < parts.length; i++) {
                            int billId = Integer.parseInt(parts[i]);
                            customerService.payBill(billId);
                            System.out.println("Payment has been completed for Bill with id " + billId);
                            System.out.println("Your current balance is: " + customerService.getBalance());
                        }
                        break;
                    case "DUE_DATE":
                        listBills(customerService.listDueBills());
                        break;
                    case "SCHEDULE":
                        int scheduleBillId = Integer.parseInt(parts[1]);
                        LocalDate scheduleDate = LocalDate.parse(parts[2], DATE_FORMATTER);
                        System.out.println("Payment for bill id " + scheduleBillId + " is scheduled on " + scheduleDate.format(DATE_FORMATTER));
                        break;
                    case "LIST_PAYMENT":
                        listPaymentHistory(customerService.listPaymentHistory());
                        break;
                    case "SEARCH_BILL_BY_PROVIDER":
                        String provider = parts[1];
                        listBills(customerService.searchBillsByProvider(provider));
                        break;
                    case "EXIT":
                        System.out.println("Good bye!");
                        return;
                    default:
                        throw new InvalidCommandException("Invalid command: " + command);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    private static void listBills(List<Bill> bills) {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        for (Bill bill : bills) {
            System.out.println(bill.getId() + ". " + bill.getType() + " " + bill.getAmount() + " " + formatDueDate(bill.getDueDate()) + " " + bill.getState() + " " + bill.getProvider());
        }
    }

    private static void billSearch(Bill bill) {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        System.out.println(bill.getId() + ". " + bill.getType() + " " + bill.getAmount() + " " + formatDueDate(bill.getDueDate()) + " " + bill.getState() + " " + bill.getProvider());
    }

    private static void listPaymentHistory(List<PaymentHistory> paymentHistory) {
        System.out.println("Bill ID     Amount  Payment Date    State   Bill Id");
        for (PaymentHistory payment : paymentHistory) {
            String paymentDetail = payment.getId() + "  " + payment.getAmount() + "     " + payment.getDatePayment() +"     "+payment.getState()+"      "+payment.getBill().getId();
            System.out.println(paymentDetail);
        }
    }

    public static String formatDueDate(LocalDate date) {
        if (date != null) {
            return date.format(DATE_FORMATTER);
        } else {
            return null;
        }
    }

    public static boolean isIdInArray(List<Bill> array, int id) {
        return array.stream().anyMatch(element -> element.getId() == id);
    }
}