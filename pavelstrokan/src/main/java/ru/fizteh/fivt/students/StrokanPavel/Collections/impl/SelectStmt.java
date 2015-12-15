package ru.fizteh.fivt.students.StrokanPavel.Collections.impl;

import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Aggregator;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by pavel on 03.12.15.
 */

public class SelectStmt<T, R> {
    private boolean isDistinct;
    private boolean isUnion;
    private boolean isJoin;
    private int maxRowsNeeded;
    private Predicate<T> whereRestriction;
    private Predicate<R> havingRestriction;
    private Function<T, ?>[] currentFunctions;
    private Function<T, ?>[] groupByExpressions;
    private Class toReturn;
    private Comparator<R>[] orderByComparators;
    private SummaryComparator<R> summaryComparator;
    private List<R> previousData;
    private List<T> currentData;
//    Stream<R> toStream;

    @SafeVarargs
    public SelectStmt(List<T> elements, Class<R> returnClass, boolean isDistinct, Function<T, ?>... functions) {
        this.previousData = new ArrayList<>();
        this.currentData = elements;
        this.toReturn = returnClass;
        this.isDistinct = isDistinct;
        this.currentFunctions = functions;
        this.maxRowsNeeded = -1;
        this.isUnion = false;
        this.isJoin = false;
    }

    public SelectStmt(List<T> elements, boolean isDistinct, Function<T, ?> first, Function<T, ?> second) {
        this.previousData = new ArrayList<>();
        this.currentData = elements;
        this.toReturn = elements.get(0).getClass();
        this.isDistinct = isDistinct;
        this.currentFunctions = new Function[]{first, second};
        this.maxRowsNeeded = -1;
        this.isUnion = false;
        this.isJoin = true;
    }

    @SafeVarargs
    public SelectStmt(List<R> pastElements, List<T> elements, Class<R> returnClass,
                      boolean isDistinct, Function<T, ?>... functions) {
        this.currentData = elements;
        this.toReturn = returnClass;
        this.isDistinct = isDistinct;
        this.currentFunctions = functions;
        this.maxRowsNeeded = -1;
        this.isUnion = true;
        this.previousData = pastElements;
    }

    public SelectStmt(List<R> pastElements, List<T> elements, boolean isDistinct, Function<T, ?> first,
                      Function<T, ?> second) {
        this.currentData = elements;
        this.toReturn = elements.get(0).getClass();
        this.isDistinct = isDistinct;
        this.currentFunctions = new Function[]{first, second};
        this.maxRowsNeeded = -1;
        this.isUnion = true;
        this.isJoin = true;
        this.previousData = pastElements;
    }



    public SelectStmt<T, R> where(Predicate<T> predicate) {
        this.whereRestriction = predicate;
        return this;
    }

    public Iterable<R> execute() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        List<R> execResult = new ArrayList<>();
        Class[] returnClasses = new Class[currentFunctions.length];
        Object[] arguments = new Object[currentFunctions.length];
        if (whereRestriction != null) {
            List<T> filteredData = currentData.stream()
                    .filter(whereRestriction::test)
                    .collect(Collectors.toList());
            currentData = filteredData;
        }
        if (groupByExpressions != null) {
            Map<Integer, Integer> mapped = new HashMap<>();
            List<List<T>> groupedElements = new ArrayList<>();
            List<Tuple<T, Integer>> grouped = new ArrayList<>();
            String[] results = new String[groupByExpressions.length];
            currentData.stream().forEach(
                    element -> {
                        for (int i = 0; i < groupByExpressions.length; i++) {
                            results[i] = (String) groupByExpressions[i].apply(element);
                        }
                        if (!mapped.containsKey(Objects.hash(results))) {
                            mapped.put(Objects.hash(results), mapped.size());
                        }
                        grouped.add(new Tuple(element, mapped.get(Objects.hash(results))));
                    }
            );
            for (int i = 0; i < mapped.size(); i++) {
                groupedElements.add(new ArrayList<T>());
            }

            for (Tuple<T, Integer> element : grouped) {
                groupedElements.get(element.getSecond()).add(element.getFirst());
            }
            for (List<T> group : groupedElements) {
                int counter = 0;
                for (Function thisFunction : this.currentFunctions) {
                    if (thisFunction instanceof Aggregator) {
                        arguments[counter] = ((Aggregator) thisFunction).apply(group);
                    } else {
                        arguments[counter] = thisFunction.apply(group.get(0));
                    }
                    returnClasses[counter] = arguments[counter].getClass();
                    ++counter;
                }
                if (isJoin) {
                    Tuple newElement = new Tuple(arguments[0], arguments[1]);
                    execResult.add((R) newElement);
                } else {
                    R newElement = (R) toReturn.getConstructor(returnClasses).newInstance(arguments);
                    execResult.add(newElement);
                }
            }
        } else {

            for (T elem : currentData) {
                int counter = 0;
                for (Function thisFunction : this.currentFunctions) {
                    List<T> thisElement = new ArrayList<>();
                    thisElement.add(elem);
                    if (thisFunction instanceof Aggregator) {
                        arguments[counter] = ((Aggregator) thisFunction).apply(thisElement);
                    } else {
                        arguments[counter] = thisFunction.apply(elem);
                    }
                    returnClasses[counter] = arguments[counter].getClass();
                    ++counter;
                }
                if (isJoin) {
                    Tuple newElement = new Tuple(arguments[0], arguments[1]);
                    execResult.add((R) newElement);
                } else {
                    R newElement = (R) toReturn.getConstructor(returnClasses).newInstance(arguments);
                    execResult.add(newElement);
                }
            }

        }

        if (havingRestriction != null) {
            List<R> filteredData = execResult.stream()
                    .filter(havingRestriction::test)
                    .collect(Collectors.toList());
        }

        if (isDistinct) {
            Set<R> distinctResults = new HashSet<R>(execResult);
            execResult.clear();
            execResult.addAll(distinctResults);
        }

        if (orderByComparators != null) {
            execResult.sort(summaryComparator);
        }

        if (maxRowsNeeded != -1) {
            if (maxRowsNeeded < execResult.size()) {
                execResult = execResult.subList(0, maxRowsNeeded);
            }
        }

        System.out.println(execResult.size());

        if (isUnion) {
            previousData.addAll(execResult);
            execResult = previousData;
        }

        System.out.println(execResult.size());

        return execResult;
    }

        @SafeVarargs
        public final SelectStmt<T, R> groupBy(Function<T, ?>... expressions) {
            this.groupByExpressions = expressions;
            return this;
        }

        @SafeVarargs
        public final SelectStmt<T, R> orderBy(Comparator<R>... comparators) {
            this.orderByComparators = comparators;
            this.summaryComparator = new SummaryComparator<>(comparators);
            return this;
        }

    public SelectStmt<T, R> having(Predicate<R> condition) {
           this.havingRestriction = condition;
            return this;
        }

    public SelectStmt<T, R> limit(int amount) {
            maxRowsNeeded = amount;
            return this;
        }

        public UnionStmt<T, R> union() throws NoSuchMethodException,
                IllegalAccessException, InstantiationException, InvocationTargetException {
            this.isUnion = true;
            List<R> answer = (List<R>) this.execute();
            System.out.println(answer.size());
            if (isJoin) {
                return new UnionStmt<>(answer, true);
            } else {
                return new UnionStmt<>(answer);
            }
        }


}
