package util;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import exception.BankException;

public class TransactionLog {
   public static synchronized void append(Path file, String line) throws BankException {
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null)
                Files.createDirectories(parent);
            Files.write(file, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new BankException("Could not write transaction log", e);
        }
    }
}
