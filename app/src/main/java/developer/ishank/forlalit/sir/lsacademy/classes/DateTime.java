package developer.ishank.forlalit.sir.lsacademy.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {

    public static String getCurrentTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.US); //MUST USE LOWERCASE 'y'. API 23- can't use uppercase
            return dateFormat.format(new Date()); // Find today date
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
