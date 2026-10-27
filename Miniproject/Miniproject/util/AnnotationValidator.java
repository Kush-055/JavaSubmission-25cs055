package util;
import java.lang.reflect.Field;
import java.util.*;

import model.annotation.MaxLength;
import model.annotation.Positive;

 public class AnnotationValidator {
    public static String[] validate(Object obj) {
        if (obj == null)
            return new String[] { "object is null" };
        List<String> errors = new ArrayList<>();
        Class<?> c = obj.getClass();
        while (c != null && c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    Object v = f.get(obj);
                    if (f.isAnnotationPresent(Positive.class) && v instanceof Number n && n.longValue() <= 0) {
                        errors.add(f.getName() + " " + f.getAnnotation(Positive.class).message());
                    }
                    if (f.isAnnotationPresent(MaxLength.class) && v != null
                            && v.toString().length() > f.getAnnotation(MaxLength.class).value()) {
                        errors.add(f.getName() + " max length is " + f.getAnnotation(MaxLength.class).value());
                    }
                } catch (ReflectiveOperationException | RuntimeException e) {
                    errors.add("could not validate " + f.getName());
                }
            }
            c = c.getSuperclass();
        }
        return errors.toArray(new String[0]);
    }
}
