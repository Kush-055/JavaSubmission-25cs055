import exception.BankException;
import exception.CloseNote;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.WithdrawRule;
import model.annotation.MaxLength;
import model.annotation.Positive;
import service.AccountStore;
import service.ConcurrencyLab;
import service.Repository;
import util.AnnotationValidator;
import util.CommandParser;
import util.Validator;

class MiniBankDemo {
    @SuppressWarnings({ "try", "unused" })
    public static void main(String[] args) throws Exception {
        Account[] a = {
                new SavingsAccount("Kush", 5000, 500),
                new CurrentAccount("Angel", 10000, 1000),
                new FixedDepositAccount("Khushi", 0)
        };
        a[0].deposit(200);
        a[1].withdraw(500);
        for (Account x : a)
            System.out.println(x.toString());
        System.out.println(a[0].equals(a[0]));
        Object obj = a[0];
        if (obj instanceof Account acc) {
            System.out.println(acc instanceof Account);
        }
        for (Account x : a)
            System.out.println(x.getType() + " rate " + x.interestRate());
        WithdrawRule r1 = new WithdrawRule() {
            @Override
            public boolean allow(Account ac, long amt) {
                return ac.getBalance() >= amt;
            }
        };
        WithdrawRule r2 = (ac, amt) -> ac.getBalance() >= amt;
        System.out.println("rule1 " + r1.allow(a[0], 100));
        System.out.println("rule2 " + r2.allow(a[0], 100));
        System.out.println("avg " + Repository.average(new Integer[] { 10, 20, 30 }));
        System.out.println(Validator.isValidMobile("9876543210"));
        try {
            System.out.println(CommandParser.parse("DEPOSIT AC0001 500"));
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
        try (CloseNote c = new CloseNote()) {
            System.out.println("try resource example");
        }
        AccountStore st = new AccountStore();
        for (Account x : a)
            st.add(x);
        System.out.println("sorted:");
        for (Account x : st.sortedByBalance())
            System.out.println(x);
        Customer c = new Customer("Kush", "Kush@example.com", "9876543210",
                new Customer.Address("Main Road", "Ahmedabad", "380001"));
        System.out.println(c.clone());
        ConcurrencyLab.producerConsumerDemo();
        ConcurrencyLab.raceConditionDemo();
        ConcurrencyLab.deadlockDemo();
        class BadSample {
            @Positive
            long balance = -5;
            @MaxLength(3)
            String name = "toolong";
        }
        for (String e : AnnotationValidator.validate(new BadSample()))
            System.out.println(e);
    }
}
