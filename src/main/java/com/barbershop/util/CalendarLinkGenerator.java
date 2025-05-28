package com.barbershop.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarLinkGenerator {

    private static final DateTimeFormatter GOOGLE_CALENDAR_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                    .withZone(ZoneOffset.UTC);

    public static String generateGoogleCalendarLink(String title, String description, String location,
                                                    ZonedDateTime start, ZonedDateTime end) {

        String startUtc = GOOGLE_CALENDAR_FORMATTER.format(start.withZoneSameInstant(ZoneOffset.UTC));
        String endUtc = GOOGLE_CALENDAR_FORMATTER.format(end.withZoneSameInstant(ZoneOffset.UTC));

        return String.format(
                "https://www.google.com/calendar/render?action=TEMPLATE&text=%s&dates=%s/%s&details=%s&location=%s",
                URLEncoder.encode(title, StandardCharsets.UTF_8),
                startUtc,
                endUtc,
                URLEncoder.encode(description, StandardCharsets.UTF_8),
                URLEncoder.encode(location, StandardCharsets.UTF_8)
        );
    }
}
