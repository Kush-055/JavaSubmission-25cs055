package util;
import exception.BankException;
import model.TransactionType;


public class CommandParser {
   public static Command parse(String line) throws BankException {
        if (line == null)
            throw new BankException("Command is empty");
        String[] p = line.trim().split("\\s+");
        if (p.length != 3)
            throw new BankException("Use: DEPOSIT account amount");
        try {
            TransactionType t = TransactionType.valueOf(p[0].toUpperCase());
            if (t == TransactionType.TRANSFER)
                throw new BankException("TRANSFER needs a destination; use menu");
            return new Command(t, p[1].toUpperCase(), Long.parseLong(p[2]));
        } catch (IllegalArgumentException e) {
            throw new BankException("Invalid command or amount", e);
        }
    }
}
