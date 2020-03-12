package com.finger.tools.util.lang;

public class ByteUtil {

    public ByteUtil() { }

    public static byte[] getBytes(byte srcBytes[], int offset, int size) throws ArrayIndexOutOfBoundsException {
        byte fieldData[] = new byte[size];
        System.arraycopy(srcBytes, offset, fieldData, 0, size);
        return fieldData;
    }

    public static String getString(byte srcBytes[], int offset, int size) throws ArrayIndexOutOfBoundsException {
        return new String(getBytes(srcBytes, offset, size));
    }

    public static String getString2(byte srcBytes[], int from, int to) throws ArrayIndexOutOfBoundsException {
        int nEndPos = to-from;
        int nLen = srcBytes.length;  

        if (nEndPos>nLen) nEndPos = nLen;

        return new String(getBytes(srcBytes, from, nEndPos));
    }

    public static String getString2(byte srcBytes[], int from) throws ArrayIndexOutOfBoundsException {
        return new String(getBytes(srcBytes, from, srcBytes.length-from));
    }
}