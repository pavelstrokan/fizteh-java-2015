import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.*;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.Student.*;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.Conditions.*;
@RunWith(MockitoJUnitRunner.class)
public class ConditionsTest extends TestCase {
    Function<Student, String> function = Student::getName;
    @Test
    public void testRlike() throws Exception {
        List<Student> toTest = new ArrayList<>();
        toTest.add(student("pushkin", LocalDate.parse("1966-02-01"), "123"));
        toTest.add(student("ptichka", LocalDate.parse("1916-05-05"), "789"));
        toTest.add(student("plushkin", LocalDate.parse("1976-04-03"), "456"));
        toTest.add(student("sinichka", LocalDate.parse("1236-08-07"), "101"));
        assertEquals(true, rlike(function, ".*in").test(toTest.get(0)));
        assertEquals(false, rlike(function, ".*ak").test(toTest.get(1)));
        assertEquals(true, rlike(function, ".*kin").test(toTest.get(2)));
        assertEquals(true, rlike(function, ".*ka").test(toTest.get(3)));
    }
}