package ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;

/**
 * Created by pavel on 30.11.15.
 */
public class Avg<T> implements Aggregator<T, Double> {

    private Function<T, ? extends Number> thisFunction;

    public Avg(Function<T, ? extends Number> givenFunction) {
        this.thisFunction = givenFunction;
    }

    @Override
    public Double apply(List<T> givenElements) {
        Double result = 0.0;
        for (T elem : givenElements) {
            result += ((Double) thisFunction.apply(elem));
        }
        return result / givenElements.size();
    }

    @Override
    public Double apply(T toCheck) {
        return ((Double) thisFunction.apply(toCheck));
    }
}
