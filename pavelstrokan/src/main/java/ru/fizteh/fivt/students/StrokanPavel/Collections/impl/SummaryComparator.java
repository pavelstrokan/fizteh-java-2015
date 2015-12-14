package ru.fizteh.fivt.students.StrokanPavel.Collections.impl;

import java.util.Comparator;

/**
 * Created by pavel on 30.11.15.
 */
public class SummaryComparator<R> implements Comparator<R> {
    Comparator<R>[] currentComparators;

    SummaryComparator(Comparator<R>... givenComparators) {
        currentComparators = givenComparators;
    }

    @Override
    public int compare(R first, R second) {
        for(Comparator<R> thisComparator : currentComparators) {
            int comparisonResult = thisComparator.compare(first, second);
            if(comparisonResult != 0) {
                return comparisonResult;
            }
        }
        return 0;
    }

}
