package com.kjb04.exchange;

import javafx.scene.control.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateParser {

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            System.err.println("Error parsing the date: " + e.getMessage());
            return null;
        }
    }
    public static Date convertStringToDateGraph(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            System.err.println("Error parsing the date: " + e.getMessage());
            return null;
        }
    }

}
