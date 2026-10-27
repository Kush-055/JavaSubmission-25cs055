package util;
import java.io.*;
import java.nio.file.*;

import exception.BankException;
import model.Account;

public class StatePersister {
    public static void save(Account[] accounts, Path file) throws BankException {
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null)
                Files.createDirectories(parent);
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
                out.writeObject(accounts);
            }
        } catch (IOException e) {
            throw new BankException("Could not save account data", e);
        }
    }

   public static Account[] load(Path file) throws BankException {
        try {
            if (!Files.exists(file) || Files.size(file) == 0)
                return new Account[0];
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
                Account[] a = (Account[]) in.readObject();
                for (Account x : a)
                    if (x != null)
                        Account.updateCounterFrom(x.getAccountNumber());
                return a;
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new BankException("Could not load account data", e);
        }
    }
}
