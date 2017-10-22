package com.pashkobohdan.promocodesmanager.util;

public class NullUtils {

    private NullUtils() {
        //Utility class
    }

    public static boolean emptyString(String text) {
        return text == null || text.intern().isEmpty();
    }

    public static String getLinesWithMaxLength(String text, int length) {
        return text.length() > length ? text.substring(0, length) : text;
    }
}
