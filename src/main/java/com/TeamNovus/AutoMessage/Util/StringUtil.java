package com.TeamNovus.AutoMessage.Util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

    public static boolean isInteger(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String concat(String[] s, int start, int end) {
        String[] args = Arrays.copyOfRange(s, start, end);
        return StringUtils.join(args, " ");
    }

    public final static long SECONDS = 60;
    public final static long MINUTES = 60;
    public final static long HOURS = 24;

    public final static long ONE_SECOND = 1000;
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long ONE_DAY = ONE_HOUR * 24;

    public static String millisToLongDHMS(long duration) {
        final StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" ").append(plural(Integer.parseInt(temp + ""), "день", "дня", "дней")).append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" ").append(plural(Integer.parseInt(temp + ""), "час", "часа", "часов")).append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" ").append(plural(Integer.parseInt(temp + ""), "минута", "минуты", "минут")).append(temp > 1 ? "s" : "");
            }

            if (!res.toString().equals("") && (duration >= ONE_SECOND)) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" ").append(plural(Integer.parseInt(temp + ""), "секунда", "секунды", "секунд")).append(temp > 1 ? "s" : "");
            }
            return res.toString();
        } else {
            return "0 секунд";
        }

    }

    public static Long parseTime(final String timeString) throws NumberFormatException {
        long time;

        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        final Pattern p = Pattern.compile("\\d+[a-z]{1}");
        final Matcher m = p.matcher(timeString);
        boolean result = m.find();

        while (result) {
            final String argument = m.group();

            if (argument.endsWith("w")) {
                weeks = Integer.parseInt(argument.substring(0, argument.length() - 1));
            } else if (argument.endsWith("d")) {
                days = Integer.parseInt(argument.substring(0, argument.length() - 1));
            } else if (argument.endsWith("h")) {
                hours = Integer.parseInt(argument.substring(0, argument.length() - 1));
            } else if (argument.endsWith("m")) {
                minutes = Integer.parseInt(argument.substring(0, argument.length() - 1));
            } else if (argument.endsWith("s")) {
                seconds = Integer.parseInt(argument.substring(0, argument.length() - 1));
            }

            result = m.find();
        }

        time = seconds;
        time += minutes * 60;
        time += hours * 3600;
        time += days * 86400;
        time += weeks * 604800;

        // convert to milliseconds
        time = time * 1000;

        return time;

    }

    /**
     * This method return plural form of word based on int value.
     *
     * @param number Int value
     * @param form1  First form of word (i == 1)
     * @param form2  Second form of word (i > 1 && i < 5)
     * @param form3  Third form of word (i > 10 && i < 20)
     * @return string
     */
    public static String plural(int number, String form1, String form2, String form3) {
        int n1 = Math.abs(number) % 100;
        int n2 = number % 10;
        if (n1 > 10 && n1 < 20) return form3;
        if (n2 > 1 && n2 < 5) return form2;
        if (n2 == 1) return form1;
        return form3;
    }
}
