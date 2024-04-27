package dev.jaegyu.QuickAlerts;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Utils {
    public static <T> T[] dropFirstItem(T[] array) throws ArrayIndexOutOfBoundsException {
        if (array.length < 1) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return Arrays.copyOfRange(array, 1, array.length);
    }

    public enum Color {
        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_AQUA('3'),
        DARK_RED('4'),
        DARK_PURPLE('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        OBFUSCATED('k'),
        BOLD('l'),
        STRIKETHROUGH('m'),
        UNDERLINE('n'),
        ITALIC('o'),
        RESET('r');

        private final char code;

        Color(char code) {
            this.code = code;
        }

        public char getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "§" + code;
        }
    }
}
