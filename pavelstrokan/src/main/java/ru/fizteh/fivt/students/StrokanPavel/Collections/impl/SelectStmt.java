package ru.fizteh.fivt.students.StrokanPavel.Collections.impl;

import javafx.util.Pair;
import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Aggregator;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by pavel on 03.12.15.
 */
@SuppressWarnings("unchecked")
public class SelectStmt<T, R> {
    private boolean isDistinct;
    private boolean isUnion;
    private boolean isJoin;
    private int maxRowsNeeded;
    private Predicate<T> whereRestriction;
    private Predicate<R> havingRestriction;
    private Function<T, ?>[] functions;
    private Function<T, ?>[] groupByExpressions;
    private Class returnClass;
    private Comparator<R>[] orderByComparators;
    private SummaryComparator<R> summaryComparator;
    private List<R> previousData;
    private List<T> data;
    public boolean isDistinct() {
        return isDistinct;
    }
    public boolean isUnion() {
        return isUnion;
    }
    public boolean isJoin() {
        return isJoin;
    }
    public Class getToReturn() {
        return returnClass;
    }
    public int getMaxRowsNeeded() {
        return maxRowsNeeded;
    }
    public Function<T, ?>[] getFunctions() {
        return functions;
    }
    public List<T> getData() {
        return data;
    }
    @SafeVarargs
    public SelectStmt(List<T> elements, Class<R> returnClass, boolean isDistinct, Function<T, ?>... functions) {
        this.previousData = new ArrayList<>();
        this.data = elements;
        this.returnClass = returnClass;
        this.isDistinct = isDistinct;
        this.functions = functions;
        this.maxRowsNeeded = -1;
        this.isUnion = false;
        this.isJoin = false;
    }

    public SelectStmt(List<T> elements, boolean isDistinct, Function<T, ?> first, Function<T, ?> second) {
        this.previousData = new ArrayList<>();
        this.data = elements;
        this.returnClass = elements.get(0).getClass();
        this.isDistinct = isDistinct;
        this.functions = new Function[]{first, second};
        this.maxRowsNeeded = -1;
        this.isUnion = false;
        this.isJoin = true;
    }

    @SafeVarargs
    public SelectStmt(List<R> pastElements, List<T> elements, Class<R> returnClass,
                      boolean isDistinct, Function<T, ?>... functions) {
        this.data = elements;
        this.returnClass = returnClass;
        this.isDistinct = isDistinct;
        this.functions = functions;
        this.maxRowsNeeded = -1;
        this.isUnion = true;
        this.previousData = pastElements;
    }

    public SelectStmt(List<R> pastElements, List<T> elements, boolean isDistinct, Function<T, ?> first,
                      Function<T, ?> second) {
        this.data = elements;
        this.returnClass = elements.get(0).getClass();
        this.isDistinct = isDistinct;
        this.functions = new Function[]{first, second};
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
        Class[] returnClasses = new Class[functions.length];
        Object[] arguments = new Object[functions.length];
        if (whereRestriction != null) {
            data = data.stream()
                    .filter(whereRestriction::test)
                    .collect(Collectors.toList());
        }
        if (groupByExpressions != null) {
            Map<Integer, Integer> mapped = new HashMap<>();
            List<List<T>> groupedElements = new ArrayList<>();
            List<Pair<T, Integer>> grouped = new ArrayList<>();
            String[] results = new String[groupByExpressions.length];
            data.stream().forEach(
                    element -> {
                        for (int i = 0; i < groupByExpressions.length; i++) {
                            results[i] = (String) groupByExpressions[i].apply(element);
                        }
                        if (!mapped.containsKey(Objects.hash(results))) {
                            mapped.put(Objects.hash(results), mapped.size());
                        }
                        grouped.add(new Pair(element, mapped.get(Objects.hash(results))));
                    }
            );
//dlya togo chtoby kazhdomu elementu iz mapa sootvetstovalo otvetvleniye
            for (int i = 0; i < mapped.size(); i++) {
                groupedElements.add(new ArrayList<T>());
            }
            for (Pair<T, Integer> element : grouped) {
                groupedElements.get(element.getValue()).add(element.getKey());
            }
            for (List<T> group : groupedElements) {
                int counter = 0;
                for (Function thisFunction : this.functions) {
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
                    R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                    execResult.add(newElement);
                }
            }
        } else {
            for (T elem : data) {
                int counter = 0;
                for (Function thisFunction : this.functions) {
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
                    R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                    execResult.add(newElement);
                }
            }
        }
        if (havingRestriction != null) {
            List<R> filteredData = execResult.stream()
                    .filter(havingRestriction::test)
                    .collect(Collectors.toList());
            execResult = filteredData;
        }
        if (isDistinct) {
            System.out.println(execResult);
            Set<Integer> hashes = new HashSet<>();
            List<R> distincted = new ArrayList<>();
            for (R element : execResult) {
                if (!hashes.contains(element.toString().hashCode())) {
                    hashes.add(element.toString().hashCode());
                    distincted.add(element);
                }
            }
            execResult = distincted;
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
        this.groupByExpressions = /*Arrays.asList*/(expressions);
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
        List<R> ans = (List<R>) this.execute();
        if (isJoin) {
            return new UnionStmt<>(ans, true);
        } else {
            return new UnionStmt<>(ans);
        }
    }


}
