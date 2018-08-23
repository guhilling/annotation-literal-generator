package de.hilling.lang.annotations;

class Utils {
    private Utils() {
    }

    private static String firstToLower(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    static String firstToUpper(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
