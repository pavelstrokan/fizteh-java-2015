package TwitterTests;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static ru.fizteh.fivt.students.StrokanPavel.TwitterStream.TextFormatter.*;

/**
 * Created by pavel on 06.12.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class TextFormatterTest extends TestCase {
    @Test
    public void testGetAppropriateForm() {
        final String[] RETWEETS_FORMS = {"ретвитов", "ретвит", "ретвита"};
        final String[] MINUTES_FORMS = {"минут", "минуту", "минуты"};
        final String[] HOURS_FORMS = {"часов", "час", "часа"};
        final String[] DAYS_FORMS = {"дней", "день", "дня"};
        assertEquals("23 ретвита", getAppropriateForm(23, RETWEETS_FORMS));
        assertEquals("17 часов", getAppropriateForm(17, HOURS_FORMS));
        assertEquals("521 минуту", getAppropriateForm(521, MINUTES_FORMS));
        assertEquals("365 дней", getAppropriateForm(365, DAYS_FORMS));
        assertEquals("221 час", getAppropriateForm(221, HOURS_FORMS));
        assertEquals("1 день", getAppropriateForm(1, DAYS_FORMS));
        assertEquals("10003 минуты", getAppropriateForm(10003, MINUTES_FORMS));
    }
    @Test
    public void testRetweetsCorrectForm() {
        assertEquals("10 ретвитов", retweetCorrectForm(10));
        assertEquals("21 ретвит", retweetCorrectForm(21));
        assertEquals("1 ретвит", retweetCorrectForm(1));
        assertEquals("33 ретвита", retweetCorrectForm(33));
    }
    @Test
    public void testMinutesCorrectForm() {
        assertEquals("10 минут назад", minutesCorrectForm(10));
        assertEquals("21 минуту назад", minutesCorrectForm(21));
        assertEquals("1 минуту назад", minutesCorrectForm(1));
        assertEquals("33 минуты назад", minutesCorrectForm(33));
    }
    @Test
    public void testHoursCorrectForm() {
        assertEquals("10 часов назад", hoursCorrectForm(10));
        assertEquals("21 час назад", hoursCorrectForm(21));
        assertEquals("1 час назад", hoursCorrectForm(1));
        assertEquals("33 часа назад", hoursCorrectForm(33));
    }
    @Test
    public void testDaysCorrectForm() {
        assertEquals("10 дней назад", daysCorrectForm(10));
        assertEquals("21 день назад", daysCorrectForm(21));
        assertEquals("1 день назад", daysCorrectForm(1));
        assertEquals("33 дня назад", daysCorrectForm(33));
    }
}