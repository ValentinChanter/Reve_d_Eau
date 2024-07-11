package com.keio.marketplace.utils;

public class CheckIntFloat {

    public static boolean checkInt(String a) {
        try {
            int test = Integer.parseInt(a);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
