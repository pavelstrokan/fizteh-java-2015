import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.fizteh.fivt.students.StrokanPavel.Collections.Sources;
import ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery;
import static ru.fizteh.fivt.students.StrokanPavel.Collections.CollectionQuery.Student.*;

@RunWith(MockitoJUnitRunner.class)
public class SourcesTest extends TestCase {
    @Test
    public void testList() throws Exception {
        List<CollectionQuery.Student> toTest = new ArrayList<>();
        toTest.add(student("ptichka", LocalDate.parse("2001-08-05"), "1234"));
        toTest.add(student("sinichka", LocalDate.parse("1337-09-11"), "456"));
        toTest.add(student("kurica", LocalDate.parse("1936-03-11"), "429"));
        toTest.add(student("drakon", LocalDate.parse("1000-01-01"), "213"));
        List<CollectionQuery.Student> toReturn = Sources.list(
                student("ptichka", LocalDate.parse("2001-08-05"), "1234"),
                student("sinichka", LocalDate.parse("1337-09-11"), "456"),
                student("kurica", LocalDate.parse("1936-03-11"), "429"),
                student("drakon", LocalDate.parse("1000-01-01"), "213"));
        assertEquals(toReturn.size(), toTest.size());
        for (int i = 0; i < toTest.size(); i++) {
            assertEquals(toReturn.get(i).toString(), toTest.get(i).toString());
        }
    }
}