package service;
import java.lang.reflect.Field;
import java.util.*;

import exception.BankException;
import model.annotation.Id;

public class Repository<T> {
    private final HashMap<String, T> data = new HashMap<>();

   public T save(T entity) throws BankException {
        if (entity == null)
            throw new BankException("Cannot save null");
        String id = readId(entity);
        if (id == null || id.isBlank())
            throw new BankException("No @Id field found");
        data.put(id, entity);
        return entity;
    }

    @SafeVarargs
   public final void saveAll(T... items) throws BankException {
        for (T x : items)
            save(x);
    }

   public T findById(String id) {
        return data.get(id);
    }

   public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

   public boolean delete(String id) {
        return data.remove(id) != null;
    }

   public int size() {
        return data.size();
    }

    private String readId(T entity) throws BankException {
        Class<?> c = entity.getClass();
        while (c != null) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    try {
                        f.setAccessible(true);
                        Object value = f.get(entity);
                        return value == null ? null : value.toString();
                    } catch (ReflectiveOperationException e) {
                        throw new BankException("Cannot read @Id field", e);
                    }
                }
            }
            c = c.getSuperclass();
        }
        throw new BankException("Object must have an @Id field");
    }

   public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0)
            return 0;
        double sum = 0;
        for (N n : values)
            sum += n.doubleValue();
        return sum / values.length;
    }
}
