package util;
import java.time.LocalDateTime;

import model.Account;

public class StatementFormatter {
    public static String buildStatement(Account a) {
        StringBuilder b = new StringBuilder();
        b.append("------ MINI BANK STATEMENT ------\n");
        b.append("Account : ").append(a.getAccountNumber()).append('\n');
        b.append("Name    : ").append(a.getOwnerName()).append('\n');
        b.append("Type    : ").append(a.getType()).append('\n');
        b.append("Balance : Rs.").append(a.getBalance()).append('\n');
        b.append("Status  : ").append(a.isActive() ? "Active" : "Closed").append('\n');
        b.append("Made at : ").append(LocalDateTime.now()).append('\n');
        return b.toString();
    }
}
