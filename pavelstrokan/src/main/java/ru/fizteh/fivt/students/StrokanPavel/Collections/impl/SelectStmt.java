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
@SuppressWarnings("unchecked")
public class SelectStmt<T, R> {
    private boolean isDistinct;
    private boolean isUnion;
    private boolean isJoin;
    private int maxRowsNeeded;
    private Predicate<T> whereRestriction;
    private Predicate<R> havingRestriction;
    private Function<T, ?>[] functions;
    private List<Function<T, ?>> groupByExpressions = new ArrayList<>();
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
        List<R> result = new LinkedList<>();
        Set<R> distinctResult = new HashSet<>();
        List<List<T>> groupedData = new LinkedList<>();
        if (whereRestriction != null) {
            data = data.stream()
                    .filter(whereRestriction::test)
                    .collect(Collectors.toList());
        }
        if (groupByExpressions.size() == 0) {
            data.forEach(element -> groupedData.add(Arrays.asList(element)));
        } else {
            Map<String, List<T>> groups = new HashMap<>();
            data.forEach(element -> {
                StringBuilder groupName = new StringBuilder();
                groupByExpressions.forEach(expression -> groupName.append(expression.apply(element).toString()));
                if (!groups.containsKey(groupName.toString())) {
                    groups.put(groupName.toString(), new LinkedList<>());
                }
                groups.get(groupName.toString()).add(element);
            });
            groups.forEach((key, value) -> groupedData.add(value));
        }
        for (List<T> group : groupedData) {
            for (int i = 0; i < functions.length; ++i) {
                if (functions[i] instanceof Aggregator) {
                    arguments[i] = ((Aggregator) functions[i]).apply(group);
                } else {
                    arguments[i] = functions[i].apply(group.get(0));
                }
                returnClasses[i] = arguments[i].getClass();
            }
            if (isJoin) {
                Tuple newElement = new Tuple(arguments[0], arguments[1]);
                execResult.add((R) newElement);
            } else {
                R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                execResult.add(newElement);
            }
        }
        if (havingRestriction != null) {
            execResult = execResult.stream()
                    .filter(havingRestriction::test)
                    .collect(Collectors.toList());
        }
        if (isDistinct) {
            Set<R> hashes = new HashSet<>(execResult);
            execResult = new ArrayList<>(hashes);
        }
        if (orderByComparators != null) {
            execResult.sort(summaryComparator);
        }
        if (maxRowsNeeded != -1) {
            if (maxRowsNeeded < execResult.size()) {
                execResult = execResult.subList(0, maxRowsNeeded);
            }
        }
        if (isUnion) {
            previousData.addAll(execResult);
            execResult = previousData;
        }
        return execResult;
    }

    @SafeVarargs
    public final SelectStmt<T, R> groupBy(Function<T, ?>... expressions) {
        this.groupByExpressions = Arrays.asList(expressions);
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
