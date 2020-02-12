package com.example.te_scheduler_c196.Utility;

import android.util.Log;

import com.example.te_scheduler_c196.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();

    //static String dateFormattedShort;

//This takes 3 entries and converts to Date format.
    public static String dateConverter(int year, int month, int day){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {month += 1;}

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateString = month + "/" + day + "/" + year;
        Date dateFormattedLong = null;
        try {
            dateFormattedLong = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateFormattedLong != null;
        return sdf.format(dateFormattedLong);

        //return dateFormattedShort;
    }


//Takes String format and parses to date.
    public static Date stringToDateConverter(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date dateFormattedLong = null;
        try{
            dateFormattedLong = sdf.parse(dateString);
        }catch (ParseException pe){
            pe.printStackTrace();
        }

        return dateFormattedLong;
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateString;
        return dateString = sdf.format(date);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Boolean compareDates(Date date1, Date date2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return sdf.format(date1).equals(sdf.format(date2));
    }
}
