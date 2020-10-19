package com.itvillage.ars;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by monirozzamanroni on 10/18/2020.
 */

public class Config {
    static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yy/MM/dd");
        Calendar calobj = Calendar.getInstance();
        return df.format(calobj.getTime());
    }

    public static Date stringToDateType(String date) {
        Date dateType = null;
        try {
            dateType = new SimpleDateFormat("yy/MM/dd").parse(date);

        } catch (ParseException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateType;
    }

    public static Date addDays(Date date, int days) {
        DateFormat df = new SimpleDateFormat("yy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return stringToDateType(df.format(cal.getTime()));
    }

    public static String dateToSting(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
        return dateFormat.format(date);

    }
}
