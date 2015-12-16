package ru.fizteh.fivt.students.StrokanPavel.Collections;

import java.util.function.Function;
import java.util.function.Predicate;

public class Conditions<T> {

    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        return elem -> expression.apply(elem).matches(regexp);
    }
    public static <T> Predicate<T> like(Function<T, String> expression, String pattern) {
        return element -> pattern.equals(expression.apply(element));
    }

}
