package info.xiaomo.core.util;

import java.io.*;
import java.nio.ByteBuffer;

public class Cast {

    private final static ByteArrayOutputStream out = new ByteArrayOutputStream();
    private static ObjectOutputStream oos;


    public static int toInteger(Object str) {
        if (str == null)
            return 0;
        if (str instanceof Number)
            return ((Number) str).intValue();
        return toInteger(str.toString());
    }

    public static double toDouble(Object number) {
        if (number == null)
            return 0L;
        if (number instanceof Number)
            return ((Number) number).doubleValue();
        else if (number instanceof String) {
            final String str = (String) number;
            if (isNumeric(str) > 0)
                return Double.valueOf(str);
            else
                return 0L;
        } else
            return 0L;
    }

    public static long toLong(Object number) {
        if (number == null)
            return 0L;
        if (number instanceof Number)
            return ((Number) number).longValue();
        else if (number instanceof String) {
            return Long.valueOf((String) number);
        } else
            return 0L;
    }

    public static int toInteger(String str) {
        if (str == null)
            return 0;
        str = str.trim();
        if (str.length() == 0)
            return 0;
        int i = isNumeric(str);
        if (i == 1)
            return Integer.parseInt(str);
        else if (i == 2)
            return Double.valueOf(str).intValue();
        else
            return 0;
    }

    /**
     * 是否为数字
     *
     * @param str str
     * @return int
     */
    public static int isNumeric(String str) {
        if (str == null)
            return 0;
        boolean isDouble = false;
        for (int i = str.length(); --i >= 0; ) {
            char c = str.charAt(i);
            if (c == '.') {
                if (isDouble) {
                    return 0;
                }
                isDouble = true;
            } else if (!Character.isDigit(str.charAt(i))) {
                return 0;
            }
        }
        if (isDouble)
            return 2;
        return 1;
    }

    public static String objectToString(Object obj) {
        if (obj.getClass().equals(String.class)) {
            return obj.toString();
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(obj);
                byte[] bytes = out.toByteArray();
                return new String(bytes, "ISO-8859-1");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static Object stringToObject(String string) {
        try {
            byte[] bytes = string.getBytes("ISO-8859-1");
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream ois;
            ois = new ObjectInputStream(in);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int bytesToInt(byte[] bytes) {
        int length = 4;
        int intValue = 0;
        for (int i = length - 1; i >= 0; i--) {
            int offset = i * 8; // 24, 16, 8
            intValue |= (bytes[i] & 0xFF) << offset;
        }
        return intValue;
    }

    public static Object bytesToObject(byte[] bytes) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream ois;
            ois = new ObjectInputStream(in);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] objectToBytes(Object obj) throws IOException {
        out.reset();
        try {
            if (oos == null)
                oos = new ObjectOutputStream(out);
            else {
                oos.reset();
            }
            oos.writeObject(obj);
            return out.toByteArray();
        } finally {
            out.close();
        }
    }

    public static byte[] stringToBytes(String str) {
        StringBuilder sb = new StringBuilder(str);
        ByteBuffer buffer = ByteBuffer.allocate(sb.length() * 2);
        int index = 0;
        while (index < sb.length()) {
            buffer.putChar(sb.charAt(index++));
        }
        return buffer.array();
    }

    public static String bytesToString(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        StringBuilder sb = new StringBuilder();
        while (buffer.hasRemaining()) {
            sb.append(buffer.getChar());
        }
        return sb.toString();
    }

    public static long combineInt2Long(int low, int high) {
        return (low & 0xFFFFFFFFL) | (((long) high << 32) & 0xFFFFFFFF00000000L);
    }

    public static int[] separateLong2int(Long val) {
        int[] ret = new int[2];
        ret[0] = (int) (0xFFFFFFFFL & val);
        ret[1] = (int) ((0xFFFFFFFF00000000L & val) >> 32);
        return ret;
    }

    public static int getLongLowInt(Long val) {
        if (val == null)
            return 0;
        return (int) (0xFFFFFFFFL & val);
    }

    public static int getLongHighInt(Long val) {
        if (val == null)
            return 0;
        return (int) ((0xFFFFFFFF00000000L & val) >> 32);
    }

    public static boolean isIntInList(int i, int[] list) {
        for (int aList : list) {
            if (aList == i)
                return true;
        }
        return false;
    }

    public static int[] stringToInts(String str, String regex) {
        String[] arr = str.split(regex);
        final int length = arr.length;
        int[] ret = new int[length];
        for (int i = 0; i < length; i++) {
            ret[i] = Cast.toInteger(arr[i]);
        }
        return ret;
    }

    /**
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static double strToDouble(String str) {
        if (str == null || str.isEmpty())
            return 0.0;
        int len = str.length();
        int p = str.indexOf('%');
        if (p == len - 1) {
            return Double.valueOf(str.substring(0, len - 1)) / 100;
        } else if (p > -1)
            return 0.0;
        if (str.equals("true"))
            return 1;
        else
            return toDouble(str);
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


}
