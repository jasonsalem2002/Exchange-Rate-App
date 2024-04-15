package com.kjb04.exchange;

import javafx.scene.control.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
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
            return null;  // or handle the error as necessary
        }
    }

    public static void main(String[] args) {
        String dateString = "Thu, 11 Apr 2024 19:18:40 GMT";
        Date date = convertStringToDate(dateString);
        System.out.println("Parsed Date: " + date);
    }

}
