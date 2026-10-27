package util;
import exception.BankException;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {
   public static String generate(Path folder, Path report) throws BankException {
        long deposits = 0, withdrawals = 0;
        StringBuilder out = new StringBuilder("MINIBANK DAILY REPORT\n");
        try {
            Files.createDirectories(folder);
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(folder, "*.log")) {
                for (Path p : ds) {
                    try (BufferedReader br = Files.newBufferedReader(p)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 3) {
                                try {
                                    long amount = Long.parseLong(parts[2].trim());
                                    if (parts[1].trim().equals("DEPOSIT"))
                                        deposits += amount;
                                    if (parts[1].trim().equals("WITHDRAW"))
                                        withdrawals += amount;
                                } catch (NumberFormatException ignored) {
                                }
                            }
                        }
                    }
                    BasicFileAttributes a = Files.readAttributes(p, BasicFileAttributes.class);
                    out.append(p.getFileName()).append(" size=").append(a.size())
                            .append(" modified=").append(a.lastModifiedTime()).append('\n');
                }
            }
            out.append("Total deposits: ").append(deposits).append('\n');
            out.append("Total withdrawals: ").append(withdrawals).append('\n');
            Path parent = report.toAbsolutePath().getParent();
            if (parent != null)
                Files.createDirectories(parent);
            Files.writeString(report, out.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return out.toString();
        } catch (IOException e) {
            throw new BankException("Could not generate report", e);
        }
    }
}
