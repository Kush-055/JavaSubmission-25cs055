package service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import model.Account;

public class AccountStore {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final List<String> history = Collections.synchronizedList(new ArrayList<>());

    public void add(Account a) {
        if (a == null)
            throw new IllegalArgumentException("Account is missing");
        accounts.put(a.getAccountNumber(), a);
    }

    public Account get(String no) {
        return accounts.get(no);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void record(String line) {
        history.add(line);
    }

    public List<String> getHistory() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> a = getAll();
        a.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return a;
    }

  public  int size() {
        return accounts.size();
    }

   public Map<String, Account> concurrentMap() {
        return accounts;
    }
}
