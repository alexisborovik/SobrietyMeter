package com.alexis.borovik.sobrietymeter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexis on 09.01.2017.
 */

public class Tools {
    public static int ChooseMessage(double promile)
    {
        if (promile < 0.7) {
           return 0;
        } else if (promile < 1.3) {
            return 1;
        } else if (promile < 2.5) {
            return 2;
        } else if (promile < 3) {
            return 3;
        } else {
            return 4;
        }
    }

    public static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String getTimeAsString()
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        return reportDate;
    }

    public static Date convertStringToDate(String datAsString)
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date r = new Date();
        try {
            r = df.parse(datAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return r;
    }
}
