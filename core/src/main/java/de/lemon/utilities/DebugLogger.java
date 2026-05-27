package de.lemon.utilities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DebugLogger {
    private static final boolean PRINT = true;

    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    public static void printInfo(String info, Object caller){
        if(!PRINT) return;
        System.out.println(GREEN + "[" + getTime() + "] " + (caller != null ? getName(caller) : "") + " " + info + RESET);
    }

    private static String getName(Object caller) {
        return caller.getClass().getSimpleName() + ":" + caller.hashCode();
    }

    public static void printInfo(String info){
        printInfo(info, null);
    }

    public static void printError(String errorMessage, Exception exception){
        if(!PRINT) return;
        System.out.println(
            RED + "[" + getTime() + "] " +
            errorMessage + " | "+
            (exception != null ? getName(exception) + ": " + exception.getMessage(): "")
            + RESET);
    }
    public static void printError(String errorMessage){
        printError(errorMessage, null);
    }

    private static String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.now().format(formatter);
    }

    public static void printList(String prefix, List list) {
        if(!PRINT) return;
        System.out.println(YELLOW + "[" + getTime() + "] " + prefix + ": " + Arrays.toString(list.toArray()) + RESET);
    }
}
