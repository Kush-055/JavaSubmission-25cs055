package model;

public class FixedDepositAccount extends Account implements InterestBearing {
    private static final long serialVersionUID = 1L;

   public FixedDepositAccount(String owner, long opening) {
        super(owner, opening, "Fixed Deposit");
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    public double yearlyInterest() {
        return getBalance() * interestRate() / 100.0;
    }
}
