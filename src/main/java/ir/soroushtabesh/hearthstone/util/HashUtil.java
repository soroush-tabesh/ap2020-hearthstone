package ir.soroushtabesh.hearthstone.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
    public static String hash(String string) {
        return DigestUtils.sha256Hex(string);
    }

    private HashUtil() {
    }
}
