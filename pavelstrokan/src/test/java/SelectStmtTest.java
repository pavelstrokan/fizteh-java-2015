import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.Student.*;
import ru.fizteh.fivt.students.StrokanPavel.Collections.*;
import ru.fizteh.fivt.students.StrokanPavel.Collections.impl.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
@RunWith(MockitoJUnitRunner.class)
public class SelectStmtTest extends TestCase {
    List<CollectionQuery.Student> toTest, emptyExampleList, distinctTest;
    Function<CollectionQuery.Student, Double> functionAge;
    Function<CollectionQuery.Student, String> functionName, functionGroup;
    CollectionQuery.Student student;
    SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select, distinctSelect;
    SelectStmt<CollectionQuery.Student, CollectionQuery.Statistics> groupSelect;
    @Before
    public void setUp() throws Exception {
        toTest = new ArrayList<>();
        emptyExampleList = new ArrayList<>();
        distinctTest = new ArrayList<>();
        toTest.add(student("ptichka", LocalDate.parse("2001-08-05"), "1234"));
        toTest.add(student("sinichka", LocalDate.parse("1337-09-11"), "456"));
        toTest.add(student("kurica", LocalDate.parse("1936-03-11"), "429"));
        toTest.add(student("drakon", LocalDate.parse("1000-01-01"), "213"));
        distinctTest.add(student("pushkin", LocalDate.parse("1966-02-01"), "123"));
        distinctTest.add(student("plushkin", LocalDate.parse("1966-02-01"), "123"));
        distinctTest.add(student("sushkin", LocalDate.parse("1916-05-05"), "789"));
        distinctTest.add(student("krujkin", LocalDate.parse("1916-05-05"), "789"));
        functionAge = CollectionQuery.Student::age;
        functionName = CollectionQuery.Student::getName;
        functionGroup = CollectionQuery.Student::getGroup;
        student = new CollectionQuery.Student("zhiraf", LocalDate.parse("1996-02-12"), "123");
        select = FromStmt.from(toTest).select(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                CollectionQuery.Student::getGroup);
        distinctSelect = FromStmt.from(distinctTest).selectDistinct(CollectionQuery.Student.class,
                CollectionQuery.Student::getName, CollectionQuery.Student::getGroup);
        groupSelect = FromStmt.from(toTest).select(CollectionQuery.Statistics.class,
                CollectionQuery.Student::getGroup, Aggregates.count(CollectionQuery.Student::getName));
    }
    @Test
    public void testWhere() throws Exception {
        List<CollectionQuery.Student> result = (List<CollectionQuery.Student>) select
                .where(s -> Objects.equals(s.getGroup(), "456"))
                .execute();
        List<CollectionQuery.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
    @Test
    public void testGroupBy() throws Exception {
        List<CollectionQuery.Statistics> result = (List<CollectionQuery.Statistics>) groupSelect
                .groupBy(CollectionQuery.Student::getGroup)
                .execute();
        List<CollectionQuery.Statistics> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Statistics("1234", 1L));
        resultList.add(new CollectionQuery.Statistics("456", 1L));
        resultList.add(new CollectionQuery.Statistics("429", 1L));
        resultList.add(new CollectionQuery.Statistics("213", 1L));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
    @Test
    public void testOrderBy() throws Exception {
        List<CollectionQuery.Student> result = (List<CollectionQuery.Student>) select
                .orderBy(OrderByConditions.desc(CollectionQuery.Student::getGroup))
                .execute();
        List<CollectionQuery.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        resultList.add(new CollectionQuery.Student("kurica", "429"));
        resultList.add(new CollectionQuery.Student("drakon", "213"));
        resultList.add(new CollectionQuery.Student("ptichka", "1234"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
    @Test
    public void testHaving() throws Exception {
        List<CollectionQuery.Statistics> result = (List<CollectionQuery.Statistics>) groupSelect
                .groupBy(CollectionQuery.Student::getGroup)
                .having(s -> Objects.equals(s.getGroup(), "1234"))
                .execute();
        System.out.println(result);
        List<CollectionQuery.Statistics> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Statistics("1234", 1L));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
    @Test
    public void testLimit() throws Exception {
        List<CollectionQuery.Student> result = (List<CollectionQuery.Student>) select
                .limit(2)
                .execute();
        List<CollectionQuery.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Student("ptichka", "1234"));
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
        result = (List<CollectionQuery.Student>) select
                .limit(5)
                .execute();
        resultList.clear();
        resultList.add(new CollectionQuery.Student("ptichka", "1234"));
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        resultList.add(new CollectionQuery.Student("kurica", "429"));
        resultList.add(new CollectionQuery.Student("drakon", "213"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
    @Test
    public void testExecute() throws Exception {
        List<CollectionQuery.Student> result = (List<CollectionQuery.Student>) select.execute();
        List<CollectionQuery.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Student("ptichka", "1234"));
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        resultList.add(new CollectionQuery.Student("kurica", "429"));
        resultList.add(new CollectionQuery.Student("drakon", "213"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
        List<CollectionQuery.Statistics> resultWithAggr = (List<CollectionQuery.Statistics>) groupSelect.execute();
        List<CollectionQuery.Statistics> resultListWithAggr = new ArrayList<>();
        resultListWithAggr.add(new CollectionQuery.Statistics("1234", 1L));
        resultListWithAggr.add(new CollectionQuery.Statistics("456", 1L));
        resultListWithAggr.add(new CollectionQuery.Statistics("429", 1L));
        resultListWithAggr.add(new CollectionQuery.Statistics("213", 1L));
        assertEquals(resultListWithAggr.size(), resultWithAggr.size());
        for (int i = 0; i < resultWithAggr.size(); i++) {
            assertEquals(resultListWithAggr.get(i).toString(), resultWithAggr.get(i).toString());
        }
    }
    @Test
    public void testUnion() throws Exception {
        List<CollectionQuery.Student> result = (List<CollectionQuery.Student>) select.union().
                from(toTest).
                select(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                        CollectionQuery.Student::getGroup).
                execute();
        List<CollectionQuery.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Student("ptichka", "1234"));
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        resultList.add(new CollectionQuery.Student("kurica", "429"));
        resultList.add(new CollectionQuery.Student("drakon", "213"));
        resultList.add(new CollectionQuery.Student("ptichka", "1234"));
        resultList.add(new CollectionQuery.Student("sinichka", "456"));
        resultList.add(new CollectionQuery.Student("kurica", "429"));
        resultList.add(new CollectionQuery.Student("drakon", "213"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
    @Test
    public void testIsDistinct() throws Exception {
        List<CollectionQuery.Student> result = (List<CollectionQuery.Student>) distinctSelect.execute();
        List<CollectionQuery.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionQuery.Student("pushkin", "123"));
        resultList.add(new CollectionQuery.Student("plushkin", "123"));
        resultList.add(new CollectionQuery.Student("sushkin", "789"));
        resultList.add(new CollectionQuery.Student("krujkin", "789"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
}