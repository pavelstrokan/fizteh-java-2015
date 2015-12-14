package ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by pavel on 30.11.15.
 */
public class Count<T> implements Aggregator<T, Long> {
    private Function<T, ?> thisFunction;
    public Count(Function<T, ?> givenFunction) {
        this.thisFunction = givenFunction;
    }

    @Override
    public Long apply(List<T> givenElements) {
        Long count = 0L;
        for(T elem : givenElements) {
            if(thisFunction.apply(elem) != null) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public Long apply(T elem) {
        if(thisFunction.apply(elem) != null) {
            return 1L;
        }
        return 0L;
    }
}
