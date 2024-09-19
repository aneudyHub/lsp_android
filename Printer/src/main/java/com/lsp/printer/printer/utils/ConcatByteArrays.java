package com.lsp.printer.printer.utils;

public class ConcatByteArrays {
    public static byte[] concatByteArrays(byte[]... arrays) {
        // Calculate total length of the concatenated array
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        // Create the result array
        byte[] result = new byte[totalLength];

        // Copy the contents of each array into the result array
        int position = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, position, array.length);
            position += array.length;
        }

        return result;
    }
}
