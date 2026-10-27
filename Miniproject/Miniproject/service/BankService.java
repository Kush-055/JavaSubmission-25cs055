package service;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.annotation.Audit;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

 public class BankService {
    private final AccountStore store = new AccountStore();
    private final Repository<Account> repo = new Repository<>();
    private final Path dataFile = Path.of("data", "accounts.bin");
    private final Path logFile = Path.of("logs", "transactions.log");

   public BankService() {
        try {
            for (Account a : StatePersister.load(dataFile)) {
                store.add(a);
                repo.save(a);
            }
        } catch (BankException ignored) {
        }
    }

    @Audit
    public Account openAccount(String name, String type, long amount) throws BankException {
        if (name == null || name.isBlank())
            throw new BankException("Name is required");
        if (amount < 0)
            throw new InvalidAmountException("Opening balance cannot be negative");
        Account a = switch (type.toLowerCase()) {
            case "s", "savings" -> {
                if (amount < 500)
                    throw new InvalidAmountException("Opening balance must be at least minimum balance (500)");
                yield new SavingsAccount(name, amount, 500);
            }
            case "c", "current" -> new CurrentAccount(name, amount, 1000);
            case "f", "fd", "fixed" -> new FixedDepositAccount(name, amount);
            default -> throw new BankException("Use S, C or F");
        };
        if (!Validator.validateRequiredFields(a))
            throw new BankException("Account validation failed");
        store.add(a);
        repo.save(a);
        log("OPEN", a, amount);
        return a;
    }

    @Audit
    public void deposit(String no, long amount) throws BankException {
        Account a = get(no);
        a.deposit(amount);
        log("DEPOSIT", a, amount);
    }

    @Audit
    public void withdraw(String no, long amount) throws BankException {
        Account a = get(no);
        a.withdraw(amount);
        log("WITHDRAW", a, amount);
    }

    @Audit
    public void transfer(String from, String to, long amount) throws BankException {
        Account a = get(from), b = get(to);
        a.transfer(b, amount);
        log("TRANSFER_OUT", a, amount);
        log("TRANSFER_IN", b, amount);
    }

   public Account get(String no) throws AccountNotFoundException {
        Account a = store.get(no);
        if (a == null)
            throw new AccountNotFoundException("Account not found: " + no);
        return a;
    }

    public String statement(String no) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(get(no));
    }

   public List<Account> listAccounts() {
        return store.sortedByBalance();
    }

   public List<String> history() {
        return store.getHistory();
    }

    public void save() throws BankException {
        StatePersister.save(store.getAll().toArray(new Account[0]), dataFile);
    }

    public String report() {
        try {
            return ReportGenerator.generate(Path.of("logs"), Path.of("reports", "daily-report.txt"));
        } catch (BankException e) {
            return "Error generating report: " + e.getMessage();
        }
    }

   public void batchDemo() {
        try (TransactionProcessor p = new TransactionProcessor()) {
            for (Account a : store.getAll()) {
                p.submit(() -> {
                    try {
                        a.deposit(1);
                    } catch (InvalidAmountException ignored) {
                    }
                });
            }
        }
        store.record("batch completed");
    }

    public void saveAllFromRepository(Account... a) throws BankException {
        repo.saveAll(a);
    }

    private void log(String type, Account a, long amount) {
        String line = LocalDateTime.now() + "|" + type + "|" + amount + "|" + a.getAccountNumber();
        try {
            TransactionLog.append(logFile, line);
        } catch (BankException e) {
            System.err.println("Warning: failed to write audit log: " + e.getMessage());
        }
        store.record(line);
    }

   public int count() {
        return store.size();
    }

    public Account find(String id) {
        return repo.findById(id);
    }
}
