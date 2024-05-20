package com.system.lsp.printer;

public class CPCLPrinter extends Printer{

    public CPCLPrinter(int dotsWidthAvailablePerRow, int dotsLineSpacing, int xStartPosition, int yStartPosition) {
        super(dotsWidthAvailablePerRow, dotsLineSpacing, xStartPosition, yStartPosition);
    }

    @Override
    public byte[] printLine(int fontSize, PrinterTextFormat textFormat, String text) {
        return new byte[0];
    }

    @Override
    public byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextAlignment alignment, String text) {
        return new byte[0];
    }

    @Override
    public byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextArrangement arrangement, String text) {
        return new byte[0];
    }

    @Override
    public byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextAlignment alignment, PrinterTextArrangement arrangement, String text) {
        return new byte[0];
    }

    @Override
    public String getFontFormat(PrinterTextFormat textFormat) {
        return null;
    }

    @Override
    public int getFontSizeInDots(int sizeInDp) {
        return 0;
    }

    @Override
    public int getMaxLineLength(int fontSizeInDots) {
        return 0;
    }

    @Override
    public String getTextAlignment(PrinterTextAlignment textAlignment, int dotsAvailable) {
        return null;
    }

    @Override
    public String[] splitLines(int fontSizeInDots, String text) {
        return new String[0];
    }

    @Override
    public byte[] printNewline() {
        return new byte[0];
    }

    @Override
    public byte[] feedPaper(int lines) {
        return new byte[0];
    }

    @Override
    public byte[] initialize() {
        return new byte[0];
    }

    @Override
    public byte[] end() {
        return new byte[0];
    }

    @Override
    public byte[] fieldOrigin(int x, int y) {
        return new byte[0];
    }
}
