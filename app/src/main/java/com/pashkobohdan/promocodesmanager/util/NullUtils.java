package com.pashkobohdan.promocodesmanager.util;

public class NullUtils {

    private NullUtils() {
        //Utility class
    }

    public static boolean emptyString(String text) {
        return text == null || text.intern().isEmpty();
    }
}
