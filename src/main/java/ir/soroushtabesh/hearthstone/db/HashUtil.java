package ir.soroushtabesh.hearthstone.db;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
    public static byte[] getHashSHA256(String string) {
        return null;
    }

    public static String hash(String string) {
        return DigestUtils.sha256Hex(string);
    }
}
