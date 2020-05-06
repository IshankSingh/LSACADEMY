package developer.ishank.forlalit.sir.lsacademy.classes;

import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CommentTime {

    public static String getTime(String timestamp) {
        return new MyTime().doInBackground(timestamp);
    }

    public static String getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssZ", Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    static class MyTime extends AsyncTask<String, String, String> {
        @Override
        protected  String doInBackground(String... strings) {
            int day, month, year, hour, min, sec, day1, month1, year1, hour1, min1, sec1, netYear, netMonth, netDay, netHour, netMin, netSec;
            String netTime;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssZ", Locale.US);
            String time = simpleDateFormat.format(calendar.getTime());

            year = Integer.valueOf(strings[0].substring(0,2));
            month = Integer.valueOf(strings[0].substring(2,4));
            day = Integer.valueOf(strings[0].substring(4,6));
            hour = Integer.valueOf(strings[0].substring(6,8));
            min = Integer.valueOf(strings[0].substring(8,10));
            sec = Integer.valueOf(strings[0].substring(10,12));

            year1 = Integer.valueOf(time.substring(0,2));
            month1 = Integer.valueOf(time.substring(2,4));
            day1 = Integer.valueOf(time.substring(4,6));
            hour1 = Integer.valueOf(time.substring(6,8));
            min1 = Integer.valueOf(time.substring(8,10));
            sec1 = Integer.valueOf(time.substring(10,12));

            netYear = year1 - year;
            netDay = day1 - day;
            netMonth = month1 - month;
            netHour = hour1 - hour;
            netMin = min1 - min;
            netSec = sec1-sec;

            if (netYear > 0) {

                if (netYear == 1) {
                    netTime = netYear + " year";
                }else {
                    netTime = netYear + " years";
                }
                return netTime;
            }

            if (netMonth > 0) {

                if (netMonth == 1) {
                    netTime = netMonth + " month";
                }else {
                    netTime = netMonth + " months";
                }
                return netTime;
            }

            if (netDay > 0) {

                if (netDay == 1) {
                    netTime = netDay + " day";
                }else {
                    netTime = netDay + " days";
                }
                return netTime;
            }

            if (netHour > 0) {

                if (netHour == 1) {
                    netTime = netHour + " hour";
                }else {
                    netTime = netHour + " hours";
                }
                return netTime;
            }

            if (netMin > 0) {

                if (netMin == 1) {
                    netTime = netMin + "min";
                }else {
                    netTime = netMin + "mins";
                }
                return netTime;
            }

            if (netSec > 0) {
                netTime = netSec + "secs";
                return netTime;
            }

            return null;
        }
    }
}
