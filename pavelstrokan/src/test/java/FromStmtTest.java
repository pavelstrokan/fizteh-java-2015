import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.StrokanPavel.Collections.impl.FromStmt;
import ru.fizteh.fivt.students.StrokanPavel.Collections.impl.SelectStmt;
import ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.Student.student;
@RunWith(MockitoJUnitRunner.class)
public class FromStmtTest extends TestCase {
    List<CollectionQuery.Student> toTest;
    List<CollectionQuery.Student> emptyTest;
    Function<CollectionQuery.Student, Double> functionAge;
    Function<CollectionQuery.Student, String> functionName, functionGroup;
    CollectionQuery.Student student;
    @Before
    public void setUp() throws Exception {
        toTest = new ArrayList<>();
        emptyTest = new ArrayList<>();
        toTest.add(student("ptichka", LocalDate.parse("2001-08-05"), "1234"));
        toTest.add(student("sinichka", LocalDate.parse("1337-09-11"), "456"));
        toTest.add(student("kurica", LocalDate.parse("1936-03-11"), "429"));
        toTest.add(student("drakon", LocalDate.parse("1000-01-01"), "213"));
        functionAge = CollectionQuery.Student::age;
        functionName = CollectionQuery.Student::getName;
        functionGroup = CollectionQuery.Student::getGroup;
        student = student("zhiraf", LocalDate.parse("1800-02-15"), "222");
    }
    @Test
    public void testFrom() throws Exception {
        FromStmt<CollectionQuery.Student> fromStmt = FromStmt.from(toTest);
        assertEquals(fromStmt.getData().size(), toTest.size());
        for (int i = 0; i < toTest.size(); i++) {
            assertEquals(toTest.get(i),fromStmt.getData().get(i));
        }
    }
    @Test
    public void testSelect() throws Exception {
        SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select = FromStmt
                .from(toTest)
                .select(CollectionQuery.Student.class, CollectionQuery.Student::age);
        assertEquals(-1, select.getMaxRowsNeeded());
        assertEquals(CollectionQuery.Student.class, select.getToReturn());
        assertEquals(false, select.isDistinct());
        assertEquals(false, select.isUnion());
        Function<CollectionQuery.Student, Double> function;
        function = CollectionQuery.Student::age;
        assertEquals(1, select.getFunctions().length);
        for (CollectionQuery.Student element : toTest) {
            assertEquals(function.apply(element),
                    select.getFunctions()[0].apply(element));
        }
        assertEquals(4, select.getData().size());
        for (int i = 0; i < toTest.size(); i++) {
            assertEquals(select.getData().get(i), toTest.get(i));
        }
    }
    @Test
    public void testSelectDistinct() throws Exception {
        SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select = FromStmt.from(toTest)
                .selectDistinct(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                        CollectionQuery.Student::getGroup);
        assertEquals(-1,select.getMaxRowsNeeded());
        assertEquals(CollectionQuery.Student.class, select.getToReturn());
        assertEquals(true,select.isDistinct());
        assertEquals(false,select.isUnion());
        Function<CollectionQuery.Student, String>[] functions = new Function[2];
        functions[0] = CollectionQuery.Student::getName;
        functions[1] = CollectionQuery.Student::getGroup;
        assertEquals(select.getFunctions().length, functions.length);
        for (int i = 0; i < functions.length; i++) {
            for (CollectionQuery.Student element : toTest) {
                assertEquals(functions[i].apply(element), select.getFunctions()[i].apply(element));
            }
        }
        assertEquals(toTest.size(), select.getData().size());
        for (int i = 0; i < toTest.size(); i++) {
            assertEquals(toTest.get(i), select.getData().get(i));
        }
    }
}