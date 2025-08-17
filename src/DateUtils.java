import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

class DateUtils {
    static String monthYearTitle(YearMonth ym) {
        String monthName = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        return monthName + " " + ym.getYear();
    }
}
