package com.giot.meeting.utils;

public class UrlParamCompleter {

    public static String complete(String url, String... args) {
        String result = url;
        int index = 1;
        for (String arg : args) {
            result = result.replace("[arg" + index + "]", arg);
            index++;
        }

        return result;
    }

}
