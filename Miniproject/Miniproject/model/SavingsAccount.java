package model;

public class SavingsAccount extends Account implements InterestBearing {
    private static final long serialVersionUID = 1L;
    private long minBalance;

    public SavingsAccount(String owner, long opening, long minBalance) {
        super(owner, opening, "Savings");
        if (minBalance < 0)
            throw new IllegalArgumentException("Minimum balance cannot be negative");
        if (opening < minBalance)
            throw new IllegalArgumentException("Opening balance must be at least minimum balance (" + minBalance + ")");
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && getBalance() - amount >= minBalance;
    }

    @Override
    public double yearlyInterest() {
        return getBalance() * interestRate() / 100.0;
    }

    public long getMinBalance() {
        return minBalance;
    }
}
