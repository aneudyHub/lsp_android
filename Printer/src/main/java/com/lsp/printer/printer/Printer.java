package com.lsp.printer.printer;

public abstract class Printer {
    protected int dotsWidthAvailablePerRow;
    protected int dotsLineSpacing;
    protected int xStartPosition;
    protected int yStartPosition;

    public char getTableSeparator() {
        return tableSeparator;
    }

    protected char tableSeparator = ';';

    public Printer(int dotsWidthAvailablePerRow, int dotsLineSpacing, int xStartPosition, int yStartPosition) {
        this.dotsWidthAvailablePerRow = dotsWidthAvailablePerRow - xStartPosition;
        this.dotsLineSpacing = dotsLineSpacing;
        this.xStartPosition = xStartPosition;
        this.yStartPosition = yStartPosition;
    }

    // Abstract method to print text
    public abstract byte[] printLine(int fontSize, PrinterTextFormat textFormat, String text);

    public abstract byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextAlignment alignment, String text);

    public abstract byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextArrangement arrangement, String text);

    public abstract byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextAlignment alignment, PrinterTextArrangement arrangement, String text);

    public abstract String getFontFormat(PrinterTextFormat textFormat);

    public abstract int getFontSizeInDots(int sizeInDp);

    public abstract int getMaxLineLength(int fontSizeInDots);

    public abstract String getTextAlignment(PrinterTextAlignment textAlignment, int dotsAvailable);

    public abstract String[] splitLines(int fontSizeInDots, String text);

    // Abstract method to print a newline
    public abstract byte[] skipLine(int fontSize, int lines);

    public abstract byte[] printTable(int fontSize, PrinterTextFormat textFormat, String[] table);

    // Abstract method to feed paper
    public abstract byte[] feedPaper(int lines);

    // Abstract method to perform printer initialization
    public abstract byte[] initialize();

    public abstract byte[] end();

    public abstract byte[] fieldOrigin(int x, int y);

    public abstract byte[] printHorizontalLine(char character);

}
