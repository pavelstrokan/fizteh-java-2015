package ru.fizteh.fivt.students.StrokanPavel.TwitterStream;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by pavel on 12/2/15.
 */
public class TimeFormatter {
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    public static String formatDifferenceBetween(long timeOfTweet, long currentTime) {
        LocalDateTime tweet = new Date(timeOfTweet).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = new Date(currentTime).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (ChronoUnit.MINUTES.between(tweet, now) < TWO) {
            return "Только что";
        }
        if (ChronoUnit.HOURS.between(tweet, now) <= ZERO) {
            long difference = ChronoUnit.MINUTES.between(tweet, now);
            return TextFormatter.minutesCorrectForm(difference);
        }
        if (ChronoUnit.DAYS.between(tweet, now) <= ZERO) {
            long difference = ChronoUnit.HOURS.between(tweet, now);
            return TextFormatter.hoursCorrectForm(difference);
        }
        if (ChronoUnit.DAYS.between(tweet, now) == ONE) {
            return "Вчера";
        }
        long difference = ChronoUnit.DAYS.between(tweet, now);
        return TextFormatter.daysCorrectForm(difference);
    }
}
