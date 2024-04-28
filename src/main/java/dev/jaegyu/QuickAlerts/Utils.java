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


        // This is also super weird imo, im not really sure if there is a better way to do this.
        // This clears the formatting so that you're only working with the unformmated text and not
        // the color/format characters.
        public static String unformatString(String s) {
            return s.replace(Utils.Color.BLACK.toString(), "")
                    .replace(Utils.Color.DARK_BLUE.toString(), "")
                    .replace(Utils.Color.DARK_GREEN.toString(), "")
                    .replace(Utils.Color.DARK_AQUA.toString(), "")
                    .replace(Utils.Color.DARK_RED.toString(), "")
                    .replace(Utils.Color.DARK_PURPLE.toString(), "")
                    .replace(Utils.Color.GOLD.toString(), "")
                    .replace(Utils.Color.GRAY.toString(), "")
                    .replace(Utils.Color.DARK_GRAY.toString(), "")
                    .replace(Utils.Color.BLUE.toString(), "")
                    .replace(Utils.Color.GREEN.toString(), "")
                    .replace(Utils.Color.AQUA.toString(), "")
                    .replace(Utils.Color.RED.toString(), "")
                    .replace(Utils.Color.LIGHT_PURPLE.toString(), "")
                    .replace(Utils.Color.YELLOW.toString(), "")
                    .replace(Utils.Color.WHITE.toString(), "")
                    .replace(Utils.Color.OBFUSCATED.toString(), "")
                    .replace(Utils.Color.BOLD.toString(), "")
                    .replace(Utils.Color.STRIKETHROUGH.toString(), "")
                    .replace(Utils.Color.UNDERLINE.toString(), "")
                    .replace(Utils.Color.ITALIC.toString(), "")
                    .replace(Utils.Color.RESET.toString(), "");
        }

        @Override
        public String toString() {
            return "§" + code;
        }
    }
}
