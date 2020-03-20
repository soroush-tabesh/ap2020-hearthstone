package ir.soroushtabesh.hearthstone.util;

import org.junit.Test;

import java.util.Arrays;

public class InputUtilTest {

    @Test
    public void tokenize() {
        System.out.println(Arrays.toString(InputUtil.tokenize("salam che-tori \"akbar pakhme\"")));
    }
}