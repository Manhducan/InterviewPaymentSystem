package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int balance;
    private List<Bill> bills = new ArrayList<>();

    public Customer() {
    }

    public Customer(int balance, List<Bill> bills) {
        this.balance = balance;
        this.bills = bills;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
