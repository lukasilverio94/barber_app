package com.barbershop.service.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CalendarLinkGenerator {

    public static String generateGoogleCalendarLink(String title, String description, String location, LocalDate date, LocalTime startTime, LocalTime endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        String startDateTime = date.format(dateFormatter) + "T" + startTime.format(timeFormatter);
        String endDateTime = date.format(dateFormatter) + "T" + endTime.format(timeFormatter);

        return String.format("https://www.google.com/calendar/render?action=TEMPLATE&text=%s&dates=%s/%s&details=%s&location=%s&sf=true&output=xml",
                URLEncoder.encode(title, StandardCharsets.UTF_8),
                startDateTime,
                endDateTime,
                URLEncoder.encode(description, StandardCharsets.UTF_8),
                URLEncoder.encode(location, StandardCharsets.UTF_8)
        );
    }
}
