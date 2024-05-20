package com.system.lsp.printer;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.system.lsp.printer.utils.TextSizeConverter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ZPLPrinter extends Printer {

    private int currentYPosition;


    public ZPLPrinter(int dotsWidthAvailablePerRow, int dotsLineSpacing, int xStartPosition, int yStartPosition) {
        super(dotsWidthAvailablePerRow, dotsLineSpacing, xStartPosition, yStartPosition);
        this.currentYPosition = yStartPosition;
    }

    @Override
    public String getFontFormat(PrinterTextFormat textFormat) {
        String fontFormat = "N";

        switch (textFormat) {
            case BOLD: {
                fontFormat = "B";
                break;
            }
            case NORMAL: {
                fontFormat = "N";
                break;
            }
            case ITALIC: {
                fontFormat = "I";
            }
        }
        return fontFormat;
    }

    @Override
    public int getFontSizeInDots(int sizeInDp) {
        return TextSizeConverter.convertSpToDots(sizeInDp);
    }

    @Override
    public int getMaxLineLength(int fontSizeInDots) {
        return (dotsWidthAvailablePerRow / fontSizeInDots);
    }

    @Override
    public String getTextAlignment(PrinterTextAlignment textAlignment, int dotsAvailable) {
        String result = "";
        switch (textAlignment) {
            case LEFT: {
                result = PrinterCommands.ALIGN_TO_LEFT.zpl(String.valueOf(dotsAvailable));
                break;
            }
            case CENTER: {
                result = PrinterCommands.ALIGN_TO_CENTER.zpl(String.valueOf(dotsAvailable));
                break;
            }

            case RIGHT: {
                result = PrinterCommands.ALIGN_TO_RIGHT.zpl(String.valueOf(dotsAvailable));
                break;
            }
        }
        return result;
    }

    @Override
    public String[] splitLines(int fontSizeInDots, String text) {
        int textSize = text.length();
        ArrayList<String> lines = new ArrayList<>();
        int startIndex = 0;
        while (startIndex < textSize) {
            int endIndex = Math.min(startIndex + getMaxLineLength(fontSizeInDots), textSize);
            String line = text.substring(startIndex, endIndex);
            lines.add(line);
            startIndex = endIndex;
        }
        return lines.toArray(new String[0]);
    }

    @Override
    public byte[] printLine(int fontSize, PrinterTextFormat textFormat, String text) {
        String fontFormatValue = getFontFormat(textFormat);
        int fontSizeInDots = TextSizeConverter.convertSpToDots(fontSize);

        String[] splitLines = splitLines(fontSizeInDots, text);
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < splitLines.length; x++) {
            int y = x * fontSizeInDots;
            stringBuilder.append(
                    createLineStringBuilder(fontSize, textFormat, splitLines[x], null, currentYPosition + y)
            );
        }
        return stringBuilder.toString().getBytes();
    }

    @Override
    public byte[] printLine(int fontSize, PrinterTextFormat textFormat, PrinterTextAlignment alignment, String text) {
        int fontSizeInDots = TextSizeConverter.convertSpToDots(fontSize);

        String[] splitLines = splitLines(fontSizeInDots, text);
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < splitLines.length; x++) {
            int y = x * fontSizeInDots;
            stringBuilder.append(
                    createLineStringBuilder(fontSize, textFormat, splitLines[x], alignment, currentYPosition + y)
            );
        }

        return stringBuilder.toString().getBytes();
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
    public byte[] printNewline() {
        return PrinterCommands.SKIP_LINE.zpl().getBytes();
    }

    @Override
    public byte[] feedPaper(int lines) {
        return PrinterCommands.FEED_PAPER.zpl(String.valueOf(lines)).getBytes();
    }

    @Override
    public byte[] initialize() {
        return PrinterCommands.START.zpl().getBytes();
    }

    @Override
    public byte[] end() {
        return PrinterCommands.END.zpl().getBytes();
    }

    @Override
    public byte[] fieldOrigin(int x, int y) {
        return PrinterCommands.FIELD_ORIGIN.zpl(String.valueOf(x), String.valueOf(y)).getBytes();
    }

    private StringBuilder createLineStringBuilder(
            int fontSize,
            PrinterTextFormat textFormat,
            String line,
            @Nullable PrinterTextAlignment textAlignment,
            int yPosition
    ) {

        /*
         * ^XA - start
         * ^FO(x position dots, y position dots) - First field
         * ^AO(text format),(width),(height)- text/size format
         * ^FD - start printing a text
         * ^FS - end printing a text
         * ^XZ - end
         *
         * */

        String fontFormatValue = getFontFormat(textFormat);
        StringBuilder stringBuilder = new StringBuilder();
        int fontSizeInDots = TextSizeConverter.convertSpToDots(fontSize);

        stringBuilder
                .append(PrinterCommands.FIELD_ORIGIN.zpl(String.valueOf(xStartPosition), String.valueOf(yPosition)))
                .append(PrinterCommands.FONT_FORMAT.zpl(fontFormatValue, String.valueOf(fontSizeInDots)));

        if (textAlignment != null) {
            stringBuilder.append(getTextAlignment(textAlignment, getMaxLineLength(fontSizeInDots)));
        }

        stringBuilder.append(PrinterCommands.START_FIELD.zpl())
                .append(line)
                .append(PrinterCommands.END_FIELD.zpl());


        return stringBuilder;
    }


}
