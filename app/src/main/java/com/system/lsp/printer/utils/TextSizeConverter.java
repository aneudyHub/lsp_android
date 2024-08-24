package com.system.lsp.printer.utils;

public class TextSizeConverter {
    // DPI of the printer
    private static final int DPI = 203;

    // Convert text size from sp to dots
    public static int convertSpToDots(int sp) {
        // Convert sp to pixels (assuming 1 sp = 1 pixel for simplicity)
        // Convert pixels to dots using DPI
        return (int) Math.round((sp * (DPI / 160.0)));
    }
}
