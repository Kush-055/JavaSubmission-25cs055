package model.annotation;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
 public @interface BankField {
    boolean required() default true;
}
