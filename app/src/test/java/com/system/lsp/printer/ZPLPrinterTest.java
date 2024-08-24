package com.system.lsp.printer;

import com.system.lsp.printer.utils.ConcatByteArrays;
import com.system.lsp.printer.utils.TextSizeConverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class ZPLPrinterTest {

    @Test
    public void testSplitLines() {
        int dotsWidthAvailablePerRow = 572;
        Printer zplPrinter = new ZPLPrinter(
                dotsWidthAvailablePerRow,
                TextSizeConverter.convertSpToDots(2),
                20,
                20
        );
        int fontSizeInDP = 14;
        int fontSizeInDots = zplPrinter.getFontSizeInDots(fontSizeInDP);
        int maxLineLength = zplPrinter.getMaxLineLength(fontSizeInDots);

        String mockTextToPrint = generateRandomString(maxLineLength * 3); // 3 lines
        int mockTextSize = mockTextToPrint.length();
        int linesToPrint = mockTextSize / maxLineLength;
        if (mockTextSize % maxLineLength != 0) {
            linesToPrint++;
        }

        String[] splitLines = zplPrinter.splitLines(fontSizeInDots, mockTextToPrint);
        Assert.assertEquals(linesToPrint, splitLines.length);
    }

    @Test
    public void testPrintlnLine() {
        int dotsWidthAvailablePerRow = 609;
        PrinterTextFormat textFormat = PrinterTextFormat.NORMAL;

        //given
        Printer zplPrinter = new ZPLPrinter(
                dotsWidthAvailablePerRow,
                TextSizeConverter.convertSpToDots(2),
                20,
                20
        );
        int fontSizeInDP = 12;
        int fontSizeInDots = zplPrinter.getFontSizeInDots(fontSizeInDP);
        int maxLineLength = zplPrinter.getMaxLineLength(fontSizeInDots);


        String mockTextToPrint = generateRandomString(maxLineLength); // 1 line
        StringBuilder expectedLine = new StringBuilder();
        expectedLine
                .append(PrinterCommands.FIELD_ORIGIN.zpl(String.valueOf(20), String.valueOf(20)))
                .append(PrinterCommands.FONT_FORMAT.zpl("N", String.valueOf(fontSizeInDots)))
                .append(PrinterCommands.START_FIELD.zpl())
                .append(mockTextToPrint)
                .append(PrinterCommands.END_FIELD.zpl());

        //then
        byte[] printLineCmd = zplPrinter.printLine(fontSizeInDP, PrinterTextFormat.NORMAL, mockTextToPrint);
        Assert.assertArrayEquals(expectedLine.toString().getBytes(), printLineCmd);

        mockTextToPrint = generateRandomString(maxLineLength * 2); // 2 line
        StringBuilder expectedLine2 = new StringBuilder();
        expectedLine2
                .append(PrinterCommands.FIELD_ORIGIN.zpl(String.valueOf(20), String.valueOf(20)))
                .append(PrinterCommands.FONT_FORMAT.zpl("N", String.valueOf(fontSizeInDots)))
                .append(PrinterCommands.START_FIELD.zpl())
                .append(mockTextToPrint.substring(0, mockTextToPrint.length() / 2))
                .append(PrinterCommands.END_FIELD.zpl())

                .append(PrinterCommands.FIELD_ORIGIN.zpl(String.valueOf(20), String.valueOf(20)))
                .append(PrinterCommands.FONT_FORMAT.zpl("N", String.valueOf(fontSizeInDots)))
                .append(PrinterCommands.START_FIELD.zpl())
                .append(mockTextToPrint.substring(mockTextToPrint.length() / 2))
                .append(PrinterCommands.END_FIELD.zpl());

        byte[] printLineCmd2 = zplPrinter.printLine(fontSizeInDP, PrinterTextFormat.NORMAL, mockTextToPrint);
        System.out.println(printLineCmd2.length);
        System.out.println(expectedLine2.toString().length());

        Assert.assertEquals(expectedLine2.toString().getBytes().length, printLineCmd2.length);


        // print with alignment to the LEFT

        PrinterTextAlignment alignment = PrinterTextAlignment.LEFT;

        mockTextToPrint = generateRandomString(maxLineLength); // 1 line

        StringBuilder expectedLine3 = new StringBuilder();
        expectedLine3
                .append(PrinterCommands.FIELD_ORIGIN.zpl(String.valueOf(20), String.valueOf(20)))
                .append(PrinterCommands.FONT_FORMAT.zpl("N", String.valueOf(fontSizeInDots)))
                .append(zplPrinter.getTextAlignment(PrinterTextAlignment.CENTER, maxLineLength))
                .append(PrinterCommands.START_FIELD.zpl())
                .append(mockTextToPrint)
                .append(PrinterCommands.END_FIELD.zpl());

        byte[] printLineCmd3 = zplPrinter.printLine(fontSizeInDP, PrinterTextFormat.NORMAL, alignment, mockTextToPrint);
        Assert.assertEquals(expectedLine3.toString().getBytes().length, printLineCmd3.length);
    }


    private String generateRandomString(int length) {
        // Define characters to be used in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Generate a random index within the range of available characters
            int randomIndex = random.nextInt(characters.length());
            // Append the random character at the random index to the string
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}
