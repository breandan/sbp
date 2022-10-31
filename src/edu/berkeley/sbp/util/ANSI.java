// Copyright 2006-2007 all rights reserved; see LICENSE file for BSD-style license

package edu.berkeley.sbp.util;
public class ANSI {
    private static final boolean disable =
        !"true".equals(System.getProperty("sbp.color", "true"));
    public static String black(Object o) { if (disable) return o+""; return o+""; }
    public static String red(Object o) { if (disable) return o+""; return "\033[31m"+o+"\033[0m"; }
    public static String green(Object o) { if (disable) return o+""; return "\033[32m"+o+"\033[0m"; }
    public static String yellow(Object o) { if (disable) return o+""; return "\033[33m"+o+"\033[0m"; }
    public static String blue(Object o) { if (disable) return o+""; return "\033[34m"+o+"\033[0m"; }
    public static String purple(Object o) { if (disable) return o+""; return "\033[35m"+o+"\033[0m"; }
    public static String cyan(Object o) { if (disable) return o+""; return "\033[36m"+o+"\033[0m"; }
    public static String invert(Object o) { if (disable) return o+""; return "\033[7m"+o+"\033[0m"; }
    public static String bold(Object o) { if (disable) return o+""; return "\033[1m"+o+"\033[0m"; }
    public static String clreol() { return "\033[0K"; }
}
