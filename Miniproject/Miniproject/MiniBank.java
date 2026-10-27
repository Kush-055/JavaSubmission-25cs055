import java.util.*;

import exception.BankException;
import exception.CloseNote;
import model.Account;
import service.BankService;

import static java.lang.System.out;

record BankInfo(String name, String branch) {
}

enum MenuOption {
    OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, STATEMENT, LIST, REPORT, BATCH, EXIT
}

public class MiniBank {
    private static final String BANK = "MiniBank";
    private static final String BRANCH = "Ahmedabad";

    @SuppressWarnings({ "try", "unused" })
    public static void main(String[] args) {
        BankInfo info = new BankInfo(BANK, BRANCH);
        out.println("\n" + info.name() + " / " + info.branch());
        BankService bank = new BankService();
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        while (run) {
            out.println("\n1.Open account  2.Deposit  3.Withdraw  4.Transfer");
            out.println("5.Statement     6.List     7.Report    8.Batch deposit  9.Exit");
            out.print("Choice: ");
            if (!sc.hasNextLine()) {
                try {
                    bank.save();
                } catch (BankException ignored) {
                }
                break;
            }
            String raw = sc.nextLine().trim();
            int n;
            try {
                n = Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                out.println("enter a number");
                continue;
            }

            MenuOption op = switch (n) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.STATEMENT;
                case 6 -> MenuOption.LIST;
                case 7 -> MenuOption.REPORT;
                case 8 -> MenuOption.BATCH;
                case 9 -> MenuOption.EXIT;
                default -> null;
            };
            if (op == null) {
                out.println("wrong choice");
                continue;
            }
            try {
                switch (op) {
                    case OPEN_ACCOUNT -> {
                        out.print("Name: ");
                        String name = sc.nextLine().trim();
                        out.print("Type S/C/F: ");
                        String type = sc.nextLine().trim();
                        out.print("Opening balance: ");
                        long amt = readAmount(sc);
                        Account a = bank.openAccount(name, type, amt);
                        out.println("opened: " + a);
                    }
                    case DEPOSIT -> {
                        out.print("Account no: ");
                        String no = sc.nextLine().trim();
                        out.print("Amount: ");
                        long amt = readAmount(sc);
                        bank.deposit(no, amt);
                        out.println("done");
                    }
                    case WITHDRAW -> {
                        out.print("Account no: ");
                        String no = sc.nextLine().trim();
                        out.print("Amount: ");
                        long amt = readAmount(sc);
                        bank.withdraw(no, amt);
                        out.println("done");
                    }
                    case TRANSFER -> {
                        out.print("From: ");
                        String from = sc.nextLine().trim();
                        out.print("To: ");
                        String to = sc.nextLine().trim();
                        out.print("Amount: ");
                        long amt = readAmount(sc);
                        bank.transfer(from, to, amt);
                        out.println("transfer done");
                    }
                    case STATEMENT -> {
                        out.print("Account no: ");
                        out.println(bank.statement(sc.nextLine().trim()));
                    }
                    case LIST -> {
                        for (Account a : bank.listAccounts())
                            out.println(a);
                        if (bank.count() == 0)
                            out.println("no accounts");
                    }
                    case REPORT -> out.println(bank.report());
                    case BATCH -> {
                        bank.batchDemo();
                        out.println("batch done");
                    }
                    case EXIT -> {
                        bank.save();
                        out.println("bye");
                        run = false;
                    }
                }
            } catch (BankException | ArithmeticException e) {
                out.println("Error: " + e.getMessage());
            } finally {
                // operation complete
            }
        }
        try (CloseNote note = new CloseNote()) {
            out.println("MiniBank ended");
        }
        sc.close();
    }

    private static long readAmount(Scanner sc) throws BankException {
        if (!sc.hasNextLine())
            throw new BankException("No amount entered");
        String s = sc.nextLine().trim();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new BankException("Enter a whole number", e);
        }
    }
}
