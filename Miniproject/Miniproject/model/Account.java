
package model;

import java.io.Serializable;
import java.util.Objects;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.BankField;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;


public abstract class Account implements Transactable, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static int counter = 0;
    @Id
    private final String accountNumber;
    @BankField
    @MaxLength(40)
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;
    private String type;

    public Account(String ownerName, long openingBalance, String type) {
        if (ownerName == null || ownerName.isBlank())
            throw new IllegalArgumentException("Owner name is required");
        if (openingBalance < 0)
            throw new IllegalArgumentException("Opening balance cannot be negative");
        synchronized (Account.class) {
            this.accountNumber = String.format("AC%04d", ++counter);
        }
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
        this.type = type;
    }

    public Account(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, "General");
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than zero");
        balance = Math.addExact(balance, amount);
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than zero");
        if (!active)
            throw new InsufficientFundsException("Account is closed");
        if (!canWithdraw(amount)) {
            long shortfall = amount > balance ? (amount - balance) : 0;
            throw new InsufficientFundsException("Withdrawal failed: short by " + shortfall, shortfall);
        }
        balance -= amount;
        return true;
    }

   public abstract double interestRate();

   public  abstract boolean canWithdraw(long amount);

    public synchronized boolean debitForTransfer(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than zero");
        if (!active)
            throw new InsufficientFundsException("Account is closed");
        if (!canWithdraw(amount)) {
            long shortfall = amount > balance ? (amount - balance) : 0;
            throw new InsufficientFundsException("Transfer rejected: short by " + shortfall, shortfall);
        }
        balance -= amount;
        return true;
    }

    public synchronized void creditForTransfer(long amount) throws InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than zero");
        balance = Math.addExact(balance, amount);
    }

   public synchronized long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean isActive() {
        return active;
    }

    public String getType() {
        return type;
    }

     public synchronized void close() {
        active = false;
    }

     public static synchronized void updateCounterFrom(String no) {
        if (no != null && no.startsWith("AC")) {
            try {
                counter = Math.max(counter, Integer.parseInt(no.substring(2)));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null)
            throw new BankException("Destination account is required");
        if (this.equals(to))
            throw new BankException("Cannot transfer to same account");
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than zero");
        Account first = this.compareTo(to) < 0 ? this : to;
        Account second = this.compareTo(to) < 0 ? to : this;
        synchronized (first) {
            synchronized (second) {
                try {
                    Math.addExact(to.getBalance(), amount);
                    this.debitForTransfer(amount);
                    to.creditForTransfer(amount);
                } catch (BankException e) {
                    throw e;
                } finally {
                    // locks released automatically
                }
            }
        }
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + type + " | Rs." + getBalance() + " | "
                + (active ? "active" : "closed");
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Account a && accountNumber.equals(a.accountNumber));
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
