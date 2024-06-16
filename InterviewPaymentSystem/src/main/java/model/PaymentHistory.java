package main.java.model;

public class PaymentHistory {
    private int id;
    private int amount;
    private String datePayment;
    private String state;
    private Bill bill;

    public PaymentHistory() {
    }

    public PaymentHistory(int id, int amount, String datePayment, String state, Bill bill) {
        this.id = id;
        this.amount = amount;
        this.datePayment = datePayment;
        this.state = state;
        this.bill = bill;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(String datePayment) {
        this.datePayment = datePayment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
