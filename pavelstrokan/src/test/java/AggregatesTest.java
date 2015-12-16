import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery;
import ru.fizteh.fivt.students.StrokanPavel.Collections.implOfAggregators.Aggregator;
import ru.fizteh.fivt.students.StrokanPavel.Collections.Aggregates;
@RunWith(MockitoJUnitRunner.class)
public class AggregatesTest extends TestCase {
    List<CollectionQuery.Student> toTest;
    List<CollectionQuery.Student> emptyTest;
    Function<CollectionQuery.Student, Double> functionAge;
    Function<CollectionQuery.Student, String> functionName, functionGroup;
    CollectionQuery.Student student;
    @Before
    public void setUp() throws Exception {
        toTest = new ArrayList<>();
        emptyTest = new ArrayList<>();
        toTest.add(new CollectionQuery.Student("ptichka", LocalDate.parse("2001-08-05"), "1234"));
        toTest.add(new CollectionQuery.Student("sinichka", LocalDate.parse("1337-09-11"), "456"));
        toTest.add(new CollectionQuery.Student("kurica", LocalDate.parse("1936-03-11"), "429"));
        toTest.add(new CollectionQuery.Student("drakon", LocalDate.parse("1000-01-01"), "213"));
        functionAge = CollectionQuery.Student::age;
        functionName = CollectionQuery.Student::getName;
        functionGroup = CollectionQuery.Student::getGroup;
        student = new CollectionQuery.Student("zhiraf", LocalDate.parse("1936-03-19"), "7885");
    }
    @Test
    public void testMax() throws Exception {
        assertEquals("456", ((Aggregator) Aggregates.max(functionGroup)).apply(toTest));
        assertEquals("sinichka", ((Aggregator) Aggregates.max(functionName)).apply(toTest));
        assertEquals(1015D, ((Aggregator) Aggregates.max(functionAge)).apply(toTest));
        assertEquals(79D, ((Aggregator) Aggregates.max(functionAge)).apply(student));
        assertEquals(null, ((Aggregator) Aggregates.max(functionAge)).apply(emptyTest));
    }
    @Test
    public void testMin() throws Exception {
        assertEquals("1234", ((Aggregator) Aggregates.min(functionGroup)).apply(toTest));
        assertEquals("drakon", ((Aggregator) Aggregates.min(functionName)).apply(toTest));
        assertEquals(14D, ((Aggregator) Aggregates.min(functionAge)).apply(toTest));
        assertEquals(79D, ((Aggregator) Aggregates.min(functionAge)).apply(student));
        assertEquals(null, ((Aggregator) Aggregates.min(functionAge)).apply(emptyTest));
    }
    @Test
    public void testCount() throws Exception {
        assertEquals(4L,((Aggregator) Aggregates.count(functionGroup)).apply(toTest));
        assertEquals(4L,((Aggregator) Aggregates.count(functionName)).apply(toTest));
        assertEquals(4L,((Aggregator) Aggregates.count(functionAge)).apply(toTest));
        assertEquals(1L,((Aggregator) Aggregates.count(functionAge)).apply(student));
    }
    @Test
    public void testAvg() throws Exception {
        assertEquals(446.5,(Double)((Aggregator) Aggregates.avg(functionAge)).apply(toTest) ,0.1);
        assertEquals(79D, ((Aggregator) Aggregates.avg(functionAge)).apply(student));
    }
}