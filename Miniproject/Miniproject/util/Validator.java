package util;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import model.annotation.BankField;

public class Validator {
    private static final Pattern MOBILE = Pattern.compile("[6-9][0-9]{9}");
    private static final Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PAN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
    private static final Pattern IFSC = Pattern.compile("[A-Z]{4}0[A-Z0-9]{6}");

    public static boolean isValidMobile(String s) {
        return s != null && MOBILE.matcher(s).matches();
    }

    public static boolean isValidEmail(String s) {
        return s != null && EMAIL.matcher(s).matches();
    }

    public static boolean isValidPan(String s) {
        return s != null && PAN.matcher(s).matches();
    }

    public static boolean isValidIfsc(String s) {
        return s != null && IFSC.matcher(s).matches();
    }

   public static boolean validateRequiredFields(Object obj) {
        if (obj == null)
            return false;
        Class<?> c = obj.getClass();
        while (c != null && c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(BankField.class)) {
                    BankField a = f.getAnnotation(BankField.class);
                    if (a.required()) {
                        try {
                            f.setAccessible(true);
                            Object val = f.get(obj);
                            if (val == null || val.toString().isBlank())
                                return false;
                        } catch (ReflectiveOperationException e) {
                            return false;
                        }
                    }
                }
            }
            c = c.getSuperclass();
        }
        return true;
    }
}
