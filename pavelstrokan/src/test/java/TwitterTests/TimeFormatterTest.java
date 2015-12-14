package TwitterTests;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.StrokanPavel.TwitterStream.TimeFormatter;
import java.util.*;

/**
 * Created by pavel on 06.12.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class TimeFormatterTest extends TestCase {
    @Test
    public void testFormatDifferenceBetween() {
        Calendar begin = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.MINUTE, 1);
        assertEquals(TimeFormatter.formatDifferenceBetween(
                begin.getTime().getTime(), end.getTime().getTime()),
                "Только что");
        end.add(Calendar.MINUTE, 2);
        assertEquals((TimeFormatter.formatDifferenceBetween(
                begin.getTime().getTime(), end.getTime().getTime())),
                "3 минуты назад");
        end.add(Calendar.HOUR, 1);
        assertEquals(TimeFormatter.formatDifferenceBetween(
                begin.getTime().getTime(), end.getTime().getTime()),
                "1 час назад");
        end.add(Calendar.DAY_OF_YEAR, 1);
        assertEquals((TimeFormatter.formatDifferenceBetween(
                begin.getTime().getTime(), end.getTime().getTime())),
                "Вчера");
        end.add(Calendar.DAY_OF_YEAR, 1);
        assertEquals((TimeFormatter.formatDifferenceBetween(
                begin.getTime().getTime(), end.getTime().getTime())),
                "2 дня назад");
    }
}