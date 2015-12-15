package ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by pavel on 30.11.15.
 */
public class Min<T, R extends Comparable<R>> implements Aggregator<T, R> {
    private Function<T, R> thisFunction;

    public Min(Function<T, R> givenFunction) {
        this.thisFunction = givenFunction;
    }

    @Override
    public R apply(List<T> elements) {
        if (elements.size() == 0) {
            return null;
        } else {
            R ourMin = thisFunction.apply(elements.get(0));
            for (T elem : elements) {
                if (ourMin.compareTo(thisFunction.apply(elem)) > 0) {
                    ourMin = thisFunction.apply(elem);
                }
            }
            return ourMin;
        }
    }

    @Override
    public R apply(T elem) {
        return thisFunction.apply(elem);
    }
}
