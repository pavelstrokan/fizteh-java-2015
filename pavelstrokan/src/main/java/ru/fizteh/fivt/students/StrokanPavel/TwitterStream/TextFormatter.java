package ru.fizteh.fivt.students.StrokanPavel.TwitterStream;


/**
 * Created by pavel on 12/2/15.
 */
public class TextFormatter {
    private static final int HUNDRED = 100;
    private static final int TWENTY = 20;
    private static final int FOUR = 4;
    private static final int ELEVEN = 11;
    private static final int TEN = 10;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final String[] RETWEETS_FORMS = {"ретвитов", "ретвит", "ретвита"};
    private static final String[] MINUTES_FORMS = {"минут", "минуту", "минуты"};
    private static final String[] HOURS_FORMS = {"часов", "час", "часа"};
    private static final String[] DAYS_FORMS = {"дней", "день", "дня"};
    public static String getAppropriateForm(long amount, String[] forms) {
        if (amount % HUNDRED >= ELEVEN
                && amount % HUNDRED <= TWENTY) {
            return amount + " " + forms[0];
        }
        if (amount % TEN == ONE) {
            return amount + " " + forms[1];
        }
        if (amount % TEN >= TWO && amount % TEN <= FOUR) {
            return amount + " " + forms[2];
        }
        return amount + " " + forms[0];
    }
    public static String retweetCorrectForm(long amount) {
        return getAppropriateForm(amount, RETWEETS_FORMS);
    }
    public static String minutesCorrectForm(long amount) {
        return getAppropriateForm(amount, MINUTES_FORMS) + " назад";
    }
    public static String hoursCorrectForm(long amount) {
        return getAppropriateForm(amount, HOURS_FORMS) + " назад";
    }
    public static String daysCorrectForm(long amount) {
        return getAppropriateForm(amount, DAYS_FORMS) + " назад";
    }
}
