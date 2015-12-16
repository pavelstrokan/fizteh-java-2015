import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery;
import ru.fizteh.fivt.students.StrokanPavel.Collections.OrderByConditions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.Student.student;
@RunWith(MockitoJUnitRunner.class)
public class OrderByTest extends TestCase {
    Function<CollectionQuery.Student, String> nameFunction = CollectionQuery.Student::getName;
    @Test
    public void testAsc() throws Exception {
        List<CollectionQuery.Student> toTest = new ArrayList<>();
        toTest.add(student("ptichka", LocalDate.parse("2001-08-05"), "1234"));
        toTest.add(student("sinichka", LocalDate.parse("1337-09-11"), "456"));
        toTest.add(student("kurica", LocalDate.parse("1936-03-11"), "429"));
        toTest.add(student("drakon", LocalDate.parse("1000-01-01"), "213"));
        assertTrue(OrderByConditions.asc(nameFunction).compare(toTest.get(0), toTest.get(1)) < 0);
        assertTrue(OrderByConditions.asc(nameFunction).compare(toTest.get(1), toTest.get(0)) > 0);
        assertTrue(OrderByConditions.asc(nameFunction).compare(toTest.get(2), toTest.get(2)) == 0);
        assertTrue(OrderByConditions.asc(nameFunction).compare(toTest.get(2), toTest.get(3)) > 0);
    }
    @Test
    public void testDesc() throws Exception {
        List<CollectionQuery.Student> toTest = new ArrayList<>();
        toTest.add(student("ptichka", LocalDate.parse("2001-08-05"), "1234"));
        toTest.add(student("sinichka", LocalDate.parse("1337-09-11"), "456"));
        toTest.add(student("kurica", LocalDate.parse("1936-03-11"), "429"));
        toTest.add(student("drakon", LocalDate.parse("1000-01-01"), "213"));
        assertTrue(OrderByConditions.desc(nameFunction).compare(toTest.get(0), toTest.get(1)) > 0);
        assertTrue(OrderByConditions.desc(nameFunction).compare(toTest.get(1), toTest.get(0)) < 0);
        assertTrue(OrderByConditions.desc(nameFunction).compare(toTest.get(2), toTest.get(2)) == 0);
        assertTrue(OrderByConditions.desc(nameFunction).compare(toTest.get(2), toTest.get(3)) < 0);
    }
}