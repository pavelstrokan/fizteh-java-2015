package ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators;


import java.util.List;
import java.util.function.Function;

/**
 * Created by pavel on 03.12.15.
 */
    public interface Aggregator<T, C> extends Function<T, C> {
        default C apply(List<T> currentElements) {
            return null;
        }
    }

