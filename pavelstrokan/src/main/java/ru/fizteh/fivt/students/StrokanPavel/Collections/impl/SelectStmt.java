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
        List<R> executeResult = new ArrayList<>();
        Class[] returnClasses = new Class[functions.length];
        Object[] arguments = new Object[functions.length];
        List<List<T>> groups = new LinkedList<>();
        if (whereRestriction != null) {
            data = data.stream()
                    .filter(whereRestriction::test)
                    .collect(Collectors.toList());
        }
        if (groupByExpressions.size() == 0) {
            data.forEach(element -> groups.add(Arrays.asList(element)));
        } else {
            Map<String, List<T>> groupsBuilder = new HashMap<>();
            data.forEach(element -> {
                StringBuilder groupName = new StringBuilder();
                groupByExpressions.forEach(expression -> groupName.append(expression.apply(element).toString()));
                if (!groupsBuilder.containsKey(groupName.toString())) {
                    groupsBuilder.put(groupName.toString(), new LinkedList<>());
                }
                groupsBuilder.get(groupName.toString()).add(element);
            });
            groupsBuilder.forEach((key, value) -> groups.add(value));
        }
        for (List<T> group : groups) {
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
                executeResult.add((R) newElement);
            } else {
                R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                executeResult.add(newElement);
            }
        }
        if (havingRestriction != null) {
            executeResult = executeResult.stream()
                    .filter(havingRestriction::test)
                    .collect(Collectors.toList());
        }
        if (isDistinct) {
            Set<R> distinct = new HashSet<>(executeResult);
            executeResult = new ArrayList<>(distinct);
        }
        if (orderByComparators != null) {
            executeResult.sort(summaryComparator);
        }
        if (maxRowsNeeded != -1) {
            if (maxRowsNeeded < executeResult.size()) {
                executeResult = executeResult.subList(0, maxRowsNeeded);
            }
        }
        if (isUnion) {
            previousData.addAll(executeResult);
            executeResult = previousData;
        }
        return executeResult;
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
