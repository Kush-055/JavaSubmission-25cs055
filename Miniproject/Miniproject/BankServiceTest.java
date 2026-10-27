import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import service.AccountWorker;
import service.BankService;
import service.Repository;
import util.AnnotationValidator;
import util.Validator;

public class BankServiceTest {
    @Test void depositAddsBalance() throws Exception {
        Account a = new SavingsAccount("Test", 100, 0);
        a.deposit(50);
        assertEquals(150, a.getBalance());
    }

    @Test void withdrawalOverLimitIsRejected() {
        Account a = new SavingsAccount("Test", 100, 50);
        assertThrows(InsufficientFundsException.class, () -> a.withdraw(75));
        assertEquals(100, a.getBalance());
    }

    @Test void shortfallRecordedOnInsufficientFundsException() {
        Account a = new CurrentAccount("ShortfallTest", 1000, 0);
        InsufficientFundsException ex = assertThrows(InsufficientFundsException.class, () -> a.withdraw(5000));
        assertEquals(4000, ex.getShortfall());
        assertEquals(1000, a.getBalance());
    }

    @Test void serviceThrowsWhenWithdrawalBreaksAccountRule() throws Exception {
        BankService s = new BankService();
        Account a = s.openAccount("JUnit", "S", 1000);
        assertThrows(InsufficientFundsException.class, () -> s.withdraw(a.getAccountNumber(), 600));
    }

    @Test void concurrentDepositsAreSafe() throws Exception {
        Account a = new CurrentAccount("Test", 0, 0);
        Thread[] ts = new Thread[10];
        for (int i = 0; i < ts.length; i++) ts[i] = new Thread(new AccountWorker(a, 1000, 1));
        for (Thread t : ts) t.start();
        for (Thread t : ts) t.join();
        assertEquals(10000, a.getBalance());
    }

    @Test void repositoryReadsIdByAnnotation() throws Exception {
        Repository<Account> r = new Repository<>();
        Account a = new SavingsAccount("Test", 10, 0);
        r.save(a);
        assertSame(a, r.findById(a.getAccountNumber()));
    }

    @Test void transferBetweenAccountsIsAtomicAndSafe() throws Exception {
        BankService s = new BankService();
        Account a1 = s.openAccount("Sender", "C", 5000);
        Account a2 = s.openAccount("Receiver", "C", 1000);
        s.transfer(a1.getAccountNumber(), a2.getAccountNumber(), 2000);
        assertEquals(3000, a1.getBalance());
        assertEquals(3000, a2.getBalance());
        assertThrows(BankException.class, () -> s.transfer(a1.getAccountNumber(), a1.getAccountNumber(), 500));
    }

    @Test void savingsAccountRejectsOpeningBalanceBelowMinimum() {
        assertThrows(IllegalArgumentException.class, () -> new SavingsAccount("Invalid", 200, 500));
    }

    @Test void currentAccountAllowsOverdraftWithinLimit() throws Exception {
        CurrentAccount ca = new CurrentAccount("Business", 1000, 2000);
        assertTrue(ca.withdraw(2500));
        assertEquals(-1500, ca.getBalance());
        assertThrows(InsufficientFundsException.class, () -> ca.withdraw(600));
        assertEquals(-1500, ca.getBalance());
    }

    @Test void fixedDepositAccountRejectsWithdrawals() {
        FixedDepositAccount fd = new FixedDepositAccount("Locked", 10000);
        assertFalse(fd.canWithdraw(100));
        assertThrows(InsufficientFundsException.class, () -> fd.withdraw(100));
        assertEquals(10000, fd.getBalance());
    }

    @Test void reflectionValidatorInspectsInheritedFields() {
        Account sa = new SavingsAccount("ValidUser", 1000, 500);
        assertTrue(Validator.validateRequiredFields(sa));

        class SubAccountWithBadName extends SavingsAccount {
            private static final long serialVersionUID = 1L;
            SubAccountWithBadName() {
                super("ToolongNameExceedingMaxLengthConstraintOfFortyCharactersLong", 1000, 500);
            }
        }
        String[] errs = AnnotationValidator.validate(new SubAccountWithBadName());
        boolean hasLengthErr = false;
        for (String err : errs) {
            if (err.contains("max length")) hasLengthErr = true;
        }
        assertTrue(hasLengthErr);
    }

    @Test void customerDeepCloneIsIndependent() {
        Customer c1 = new Customer("Kush", "Kush@test.com", "9876543210",
                new Customer.Address("123 Street", "Ahmedabad", "380001"));
        Customer c2 = c1.clone();
        assertNotSame(c1, c2);
        assertNotSame(c1.getAddress(), c2.getAddress());
        assertEquals(c1.getAddress(), c2.getAddress());
    }

    @Test void interestCalculationForInterestBearingAccounts() {
        SavingsAccount sa = new SavingsAccount("Saver", 10000, 500);
        assertEquals(4.0, sa.interestRate());
        assertEquals(400.0, sa.yearlyInterest());

        FixedDepositAccount fd = new FixedDepositAccount("Depositor", 10000);
        assertEquals(7.0, fd.interestRate());
        assertEquals(700.0, fd.yearlyInterest());
    }

    @Test void persistenceRoundTripPreservesAccounts() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("accounts_test", ".bin");
        try {
            Account[] original = {
                new SavingsAccount("PersistUser1", 2000, 500),
                new CurrentAccount("PersistUser2", 5000, 1000)
            };
            util.StatePersister.save(original, tempFile);
            Account[] reloaded = util.StatePersister.load(tempFile);
            assertEquals(2, reloaded.length);
            assertEquals(original[0].getAccountNumber(), reloaded[0].getAccountNumber());
            assertEquals(original[0].getBalance(), reloaded[0].getBalance());
            assertEquals(original[1].getAccountNumber(), reloaded[1].getAccountNumber());
            assertEquals(original[1].getBalance(), reloaded[1].getBalance());
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    public static void main(String[] args) throws Exception {
        BankServiceTest test = new BankServiceTest();
        System.out.println("Running BankServiceTest...");
        test.depositAddsBalance();
        System.out.println("  PASS: depositAddsBalance");
        test.withdrawalOverLimitIsRejected();
        System.out.println("  PASS: withdrawalOverLimitIsRejected");
        test.shortfallRecordedOnInsufficientFundsException();
        System.out.println("  PASS: shortfallRecordedOnInsufficientFundsException");
        test.serviceThrowsWhenWithdrawalBreaksAccountRule();
        System.out.println("  PASS: serviceThrowsWhenWithdrawalBreaksAccountRule");
        test.concurrentDepositsAreSafe();
        System.out.println("  PASS: concurrentDepositsAreSafe");
        test.repositoryReadsIdByAnnotation();
        System.out.println("  PASS: repositoryReadsIdByAnnotation");
        test.transferBetweenAccountsIsAtomicAndSafe();
        System.out.println("  PASS: transferBetweenAccountsIsAtomicAndSafe");
        test.savingsAccountRejectsOpeningBalanceBelowMinimum();
        System.out.println("  PASS: savingsAccountRejectsOpeningBalanceBelowMinimum");
        test.currentAccountAllowsOverdraftWithinLimit();
        System.out.println("  PASS: currentAccountAllowsOverdraftWithinLimit");
        test.fixedDepositAccountRejectsWithdrawals();
        System.out.println("  PASS: fixedDepositAccountRejectsWithdrawals");
        test.reflectionValidatorInspectsInheritedFields();
        System.out.println("  PASS: reflectionValidatorInspectsInheritedFields");
        test.customerDeepCloneIsIndependent();
        System.out.println("  PASS: customerDeepCloneIsIndependent");
        test.interestCalculationForInterestBearingAccounts();
        System.out.println("  PASS: interestCalculationForInterestBearingAccounts");
        test.persistenceRoundTripPreservesAccounts();
        System.out.println("  PASS: persistenceRoundTripPreservesAccounts");
        System.out.println("\nAll 14 direct tests passed successfully!");

        System.out.println("\nExecuting via JUnit 5 Platform Launcher Engine...");
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(BankServiceTest.class))
                .build();
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        TestExecutionSummary summary = listener.getSummary();
        System.out.printf("JUnit 5 Platform Summary: Found=%d, Succeeded=%d, Failed=%d%n",
                summary.getTestsFoundCount(),
                summary.getTestsSucceededCount(),
                summary.getTestsFailedCount());
    }
}
