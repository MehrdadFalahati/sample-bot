package ir.asta.samplebot.util.date;
import com.ibm.icu.util.Calendar;
import java.util.Date;


public class JalaliCalendarUtil {
    /**
     * @param year
     * @param month
     *            //starts from 0
     * @param day
     * @return
     */
    public static boolean isValidDate(int year, int month, int day) {

        int monthLength[] = new int[] { 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29 };

        int mLength;

        if (month < 0 || month > 11 || day < 0 || day > 31 || year < 0)
            return false;

        if (month < 11)
            mLength = monthLength[month];

        else if (IsLeapYear(year))
            mLength = 30;
        else
            mLength = 29;

        if (day > mLength)
            return false;

        return true;

    }

    public static boolean IsLeapYear(int year) {
        return ((((year + 38) * 31) % 128) <= 30);
    }

    /**
     * add specified amount of month to a JalaliDate. note that in JalaliDate,
     * all months starts from 1 to 12.
     *
     * @param input
     * @param amount
     * @return
     */
    public static Date addMonths(Date input, int amount) {
        JalaliCalendar cal = new JalaliCalendar();
        cal.setTime(input);
        cal.add(Calendar.MONTH, amount);
        return cal.getTime();
    }

}
