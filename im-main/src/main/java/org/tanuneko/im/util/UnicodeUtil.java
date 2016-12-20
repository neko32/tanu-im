package org.tanuneko.im.util;

/**
 * Created by neko32 on 2016/12/13.
 */
@SuppressWarnings("ALL")
public class UnicodeUtil {

    private UnicodeUtil() {
        throw new IllegalStateException("no use");
    }

    public static int getByteOfChar(char c) {
        int retval = 1;
        if ((c <= '\u007e') || // ascii
        (c == '\u00a5') || // \
        (c == '\u203e') || // ~
        (c >= '\uff61' && c <= '\uff9f')) { // hankaku kana
            retval = 1;
        } else {
            retval = 2;
        }
        return retval;
    }

    public static int getTotalByteCounts(final String str) {
        int total = 0;
        for(char ch: str.toCharArray()) {
            total += getByteOfChar(ch);
        }
        return total;
    }

    public static boolean isLikely2ByteString(final String str) {
        int num2Bytes = 0;
        int num1Byte = 0;
        for(char ch: str.toCharArray()) {
            if(getByteOfChar(ch) == 1) {
                num1Byte++;
            } else {
                num2Bytes++;
            }
        }
    //    return num2Bytes > num1Byte ? true : false;
        return num2Bytes > 2;
    }
}
