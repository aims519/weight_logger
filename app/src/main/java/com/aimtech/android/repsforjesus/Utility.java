package com.aimtech.android.repsforjesus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andy on 01/08/2016.
 * Various helper functions that can be used throughout the project
 */
public class Utility {

    public static String formatDateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static Date parseStringToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        try{
            return dateFormat.parse(dateString);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }
}
