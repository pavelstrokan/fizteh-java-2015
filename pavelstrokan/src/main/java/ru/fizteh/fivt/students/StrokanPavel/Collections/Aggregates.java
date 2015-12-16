package ru.fizteh.fivt.students.StrokanPavel.Collections;

import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Avg;
import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Count;
import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Max;
import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Min;

import java.util.function.Function;
public class Aggregates {

    public static <T, R extends Comparable> Function<T, R> max(Function<T, R> expression) {
        return new Max<>(expression);
    }
    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        return new Min<>(expression);
    }
    public static <T> Function<T, Long> count(Function<T, ?> expression) {
        return new Count<>(expression);
    }
    public static <T> Function<T, Double> avg(Function<T, Double> expression) {
        return new Avg<>(expression);
    }

}
