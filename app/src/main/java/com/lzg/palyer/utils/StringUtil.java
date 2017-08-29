package com.lzg.palyer.utils;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Some common string manipulation utilities.
 */
public final class StringUtil {

    public static final String EMPTY_STRING = "";

    public static final String WHITE_SPACES = " \r\n\t\u3000\u00A0\u2007\u202F";

    public static final String SPACE = " ";

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;

    // This class should not be instantiated, hence the private constructor
    private StringUtil() {
    }


    /**
     * Split "str" by run of delimiters and return.
     */
    public static String[] split(String str, String delims) {
        return split(str, delims, false);
    }

    /**
     * Split "str" into tokens by delimiters and optionally remove white spaces
     * from the splitted tokens.
     *
     * @param trimTokens if true, then trim the tokens
     */
    public static String[] split(String str, String delims, boolean trimTokens) {
        StringTokenizer tokenizer = new StringTokenizer(str, delims);
        int n = tokenizer.countTokens();
        String[] list = new String[n];
        for (int i = 0; i < n; i++) {
            if (trimTokens) {
                list[i] = tokenizer.nextToken().trim();
            } else {
                list[i] = tokenizer.nextToken();
            }
        }
        return list;
    }

    /**
     * Short hand for <code>split(str, delims, true)</code>
     */
    public static String[] splitAndTrim(String str, String delims) {
        return split(str, delims, true);
    }

    /**
     * Parse comma-separated list of ints and return as array.
     */
    public static int[] splitInts(String str) throws IllegalArgumentException {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        int n = tokenizer.countTokens();
        int[] list = new int[n];
        for (int i = 0; i < n; i++) {
            String token = tokenizer.nextToken();
            list[i] = Integer.parseInt(token);
        }
        return list;
    }

    /**
     * Parse comma-separated list of longs and return as array.
     */
    public static long[] splitLongs(String str) throws IllegalArgumentException {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        int n = tokenizer.countTokens();
        long[] list = new long[n];
        for (int i = 0; i < n; i++) {
            String token = tokenizer.nextToken();
            list[i] = Long.parseLong(token);
        }
        return list;
    }

    /**
     * Concatenates the given int[] array into one String, inserting a delimiter
     * between each pair of elements.
     */
    public static String joinInts(int[] tokens, String delimiter) {
        if (tokens == null) return "";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            if (i > 0 && delimiter != null) {
                result.append(delimiter);
            }
            result.append(String.valueOf(tokens[i]));
        }
        return result.toString();
    }

    /**
     * Concatenates the given long[] array into one String, inserting a delimiter
     * between each pair of elements.
     */
    public static String joinLongs(long[] tokens, String delimiter) {
        if (tokens == null) return "";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            if (i > 0 && delimiter != null) {
                result.append(delimiter);
            }
            result.append(String.valueOf(tokens[i]));
        }
        return result.toString();
    }

    /**
     * Concatenates the String representations of the elements of a
     * String[] array into one String, and inserts a delimiter between
     * each pair of elements.
     * <p>
     * This includes the String[] case, because if s is a String, then
     * s.toString() returns s.
     *
     * @deprecated Please use
     * But note that {@code Join} does not consider null elements to be
     * equivalent to the empty string, as this method does.
     */
    @Deprecated
    public static String join(Object[] tokens, String delimiter) {
        if (tokens == null || tokens.length == 0) return "";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            if (i > 0 && delimiter != null)
                result.append(delimiter);

            if (tokens[i] != null)
                result.append(tokens[i].toString());
        }
        return result.toString();
    }

    /**
     * Same as {@link #join(Object[], String)}, but takes a {@link Collection}
     * instead.
     *
     * @deprecated Please use
     * But note that {@code Join} does not consider null elements to be
     * equivalent to the empty string, as this method does.
     */
    @Deprecated
    public static String join(Collection tokens, String delimiter) {
        if (tokens == null) return EMPTY_STRING;

        return join(tokens.toArray(), delimiter);
    }

    /**
     * This replaces the occurances of 'what' in 'str' with 'with'
     *
     * @param str  - the string o process
     * @param what - to replace
     * @param with - replace with this
     * @return String str whete 'what' was repalced with 'with'
     * @deprecated Please use {@link String#replace(CharSequence, CharSequence)}.
     */
    @Deprecated
    public static String replace(String str, String what, String with) {
        // Have to check this argument, for compatibility with the old impl.
        // For the record, String.replace() is capable of handling an empty target
        // string... but it does something kind of weird in that case.
        assert (what.length() > 0);
        return str.replace(what, with);
    }

    /**
     * Reformats the given string to a fixed width by inserting
     * carriage returns and trimming unnecessary whitespace.
     *
     * @param str   the string to format
     * @param width the fixed width (in characters)
     */
    public static String fixedWidth(String str, int width) {
        String[] lines = split(str, "\n");
        return fixedWidth(lines, width);
    }

    /**
     * Reformats the given array of lines to a fixed width by inserting
     * carriage returns and trimming unnecessary whitespace.
     *
     * @param lines - array of lines to format
     * @param width - the fixed width (in characters)
     */
    public static String fixedWidth(String[] lines, int width) {
        StringBuilder formatStr = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            int curWidth = 0;
            if (i != 0) {
                formatStr.append("\n");
            }
            // a small optimization
            if (lines[i].length() <= width) {
                formatStr.append(lines[i]);
                continue;
            }
            String[] words = splitAndTrim(lines[i], WHITE_SPACES);
            for (int j = 0; j < words.length; j++) {
                if (curWidth == 0 || (curWidth + words[j].length()) < width) {
                    // add a space if we're not at the beginning of a line
                    if (curWidth != 0) {
                        formatStr.append(" ");
                        curWidth += 1;
                    }
                    curWidth += words[j].length();
                    formatStr.append(words[j]);
                } else {
                    formatStr.append("\n");
                    curWidth = words[j].length();
                    formatStr.append(words[j]);
                }
            }
        }

        return formatStr.toString();
    }

    /**
     * Inserts spaces every splitLen characters so that the string will wrap.
     *
     * @param lineLen  the length of the substrings to separate with spaces.
     * @param original the original String
     * @return original String with spaces inserted every lineLen characters.
     */
    public static String insertBreakingWhitespace(int lineLen, String original) {
        if (original == null || lineLen <= 0) throw new IllegalArgumentException();

        int length = original.length();
        if (length <= lineLen)
            // we can avoid the overhead of instantiating a StringBuilder
            return original;
        int currPos = 0;
        StringBuilder retval = new StringBuilder();
        while (length - currPos > lineLen) {
            retval.append(original.substring(currPos, currPos + lineLen));
            currPos += lineLen;
            retval.append(" ");
        }
        retval.append(original.substring(currPos, length));
        return retval.toString();
    }

    /**
     * Indents the given String per line.
     *
     * @param iString      The string to indent.
     * @param iIndentDepth The depth of the indentation.
     * @return The indented string.
     */
    public static String indent(String iString, int iIndentDepth) {
        StringBuilder spacer = new StringBuilder();
        spacer.append("\n");
        for (int i = 0; i < iIndentDepth; i++) {
            spacer.append("  ");
        }
        return replace(iString, "\n", spacer.toString());
    }

    /**
     * This is a both way strip
     *
     * @param str   the string to strip
     * @param left  strip from left
     * @param right strip from right
     * @param what  character(s) to strip
     * @return the stripped string
     */
    public static String megastrip(String str,
                                   boolean left, boolean right,
                                   String what) {
        if (str == null) {
            return null;
        }

        int limitLeft = 0;
        int limitRight = str.length() - 1;

        while (left && limitLeft <= limitRight &&
                what.indexOf(str.charAt(limitLeft)) >= 0) {
            limitLeft++;
        }
        while (right && limitRight >= limitLeft &&
                what.indexOf(str.charAt(limitRight)) >= 0) {
            limitRight--;
        }

        return str.substring(limitLeft, limitRight + 1);
    }

    /**
     * lstrip - strips spaces from left
     *
     * @param str what to strip
     * @return String the striped string
     */
    public static String lstrip(String str) {
        return megastrip(str, true, false, WHITE_SPACES);
    }

    /**
     * rstrip - strips spaces from right
     *
     * @param str what to strip
     * @return String the striped string
     */
    public static String rstrip(String str) {
        return megastrip(str, false, true, WHITE_SPACES);
    }

    /**
     * strip - strips both ways
     *
     * @param str what to strip
     * @return String the striped string
     */
    public static String strip(String str) {
        return megastrip(str, true, true, WHITE_SPACES);
    }

    /**
     * Strip white spaces from both end, and collapse white spaces
     * in the middle.
     *
     * @param str what to strip
     * @return String the striped and collapsed string
     */
    public static String stripAndCollapse(String str) {
        return collapseWhitespace(strip(str));
    }

    /**
     * Give me a string and a potential prefix, and I return the string
     * following the prefix if the prefix matches, else null.
     * Analogous to the c++ functions strprefix and var_strprefix.
     */
    public static String stripPrefix(String str, String prefix) {
        return str.startsWith(prefix) ? str.substring(prefix.length()) : null;
    }

    /**
     * Case insensitive version of stripPrefix.
     * Analogous to the c++ functions strcaseprefix and var_strcaseprefix.
     */
    public static String stripPrefixIgnoreCase(String str, String prefix) {
        if (str.length() >= prefix.length()
                && str.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
            return str.substring(prefix.length());
        }

        return null;
    }

    /**
     * Strips all non-digit characters from a string.
     * <p>
     * The resulting string will only contain characters for which isDigit()
     * returns true.
     *
     * @param str the string to strip
     * @return a string consisting of digits only, or an empty string
     */
    public static String stripNonDigits(String str) {
        StringBuffer result = new StringBuffer(str.length());
        for (char candidate : str.toCharArray()) {
            if (Character.isDigit(candidate)) {
                result.append(candidate);
            }
        }
        return result.toString();
    }

    /**
     * Counts the number of (not necessarily distinct) characters in the
     * string that also happen to be in 'chars'
     */
    public static int numSharedChars(final String str, final String chars) {

        if (str == null || chars == null) {
            return 0;
        }

        int total = 0, pos = -1;
        while ((pos = indexOfChars(str, chars, pos + 1)) != -1) {
            total++;
        }

        return total;
    }

    /**
     * Like String.indexOf() except that it will look for any of the
     * characters in 'chars' (similar to C's strpbrk)
     */
    public static int indexOfChars(String str, String chars, int fromIndex) {
        final int len = str.length();

        for (int pos = fromIndex; pos < len; pos++) {
            if (chars.indexOf(str.charAt(pos)) >= 0) {
                return pos;
            }
        }

        return -1;
    }

    /**
     * Like String.indexOf() except that it will look for any of the
     * characters in 'chars' (similar to C's strpbrk)
     */
    public static int indexOfChars(String str, String chars) {
        return indexOfChars(str, chars, 0);
    }

    /**
     * Finds the last index in str of a character not in the characters
     * in 'chars' (similar to ANSI string.find_last_not_of).
     * <p>
     * Returns -1 if no such character can be found.
     */
    public static int lastIndexNotOf(String str, String chars, int fromIndex) {
        fromIndex = Math.min(fromIndex, str.length() - 1);

        for (int pos = fromIndex; pos >= 0; pos--) {
            if (chars.indexOf(str.charAt(pos)) < 0) {
                return pos;
            }
        }

        return -1;
    }

    /**
     * Like String.replace() except that it accepts any number of old chars.
     * Replaces any occurrances of 'oldchars' in 'str' with 'newchar'.
     * Example: replaceChars("Hello, world!", "H,!", ' ') returns " ello  world "
     */
    public static String replaceChars(String str, String oldchars, char newchar) {
        int pos = indexOfChars(str, oldchars);
        if (pos == -1) {
            return str;
        }

        StringBuilder buf = new StringBuilder(str);
        do {
            buf.setCharAt(pos, newchar);
            pos = indexOfChars(str, oldchars, pos + 1);
        } while (pos != -1);

        return buf.toString();
    }

    /**
     * Remove any occurrances of 'oldchars' in 'str'.
     * Example: removeChars("Hello, world!", ",!") returns "Hello world"
     */
    public static String removeChars(String str, String oldchars) {
        int pos = indexOfChars(str, oldchars);
        if (pos == -1) {
            return str;
        }

        StringBuilder buf = new StringBuilder();
        int start = 0;
        do {
            buf.append(str.substring(start, pos));
            start = pos + 1;
            pos = indexOfChars(str, oldchars, start);
        } while (pos != -1);

        if (start < str.length()) {
            buf.append(str.substring(start));
        }
        return buf.toString();
    }

    /**
     * Removes all characters from 'str' that are not in 'retainChars'.
     * Example: retainAllChars("Hello, world!", "lo") returns "llool"
     */
    public static String retainAllChars(String str, String retainChars) {
        int pos = indexOfChars(str, retainChars);
        if (pos == -1) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(str.charAt(pos));
            pos = indexOfChars(str, retainChars, pos + 1);
        } while (pos != -1);
        return buf.toString();
    }

    /**
     * Replaces microsoft "smart quotes" (curly " and ') with their
     * ascii counterparts.
     */
    public static String replaceSmartQuotes(String str) {
        // See http://www.microsoft.com/typography/unicode/1252.htm
        str = replaceChars(str, "\u0091\u0092\u2018\u2019", '\'');
        str = replaceChars(str, "\u0093\u0094\u201c\u201d", '"');
        return str;
    }

    /**
     * Convert a string of hex digits to a byte array, with the first
     * byte in the array being the MSB. The string passed in should be
     * just the raw digits (upper or lower case), with no leading
     * or trailing characters (like '0x' or 'h').
     * An odd number of characters is supported.
     * If the string is empty, an empty array will be returned.
     * <p>
     * This is significantly faster than using
     * new BigInteger(str, 16).toByteArray();
     * especially with larger strings. Here are the results of some
     * microbenchmarks done on a P4 2.8GHz 2GB RAM running
     * linux 2.4.22-gg11 and JDK 1.5 with an optimized build:
     * <p>
     * String length        hexToBytes (usec)   BigInteger
     * -----------------------------------------------------
     * 16                       0.570                 1.43
     * 256                      8.21                 44.4
     * 1024                    32.8                 526
     * 16384                  546                121000
     */
    public static byte[] hexToBytes(String str) {
        byte[] bytes = new byte[(str.length() + 1) / 2];
        if (str.length() == 0) {
            return bytes;
        }
        bytes[0] = 0;
        int nibbleIdx = (str.length() % 2);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!isHex(c)) {
                throw new IllegalArgumentException("string contains non-hex chars");
            }
            if ((nibbleIdx % 2) == 0) {
                bytes[nibbleIdx >> 1] = (byte) (hexValue(c) << 4);
            } else {
                bytes[nibbleIdx >> 1] += (byte) hexValue(c);
            }
            nibbleIdx++;
        }
        return bytes;
    }

    /**
     * Converts any instances of "\r" or "\r\n" style EOLs into "\n" (Line Feed).
     */
    public static String convertEOLToLF(String input) {
        StringBuilder res = new StringBuilder(input.length());
        char[] s = input.toCharArray();
        int from = 0;
        final int end = s.length;
        for (int i = 0; i < end; i++) {
            if (s[i] == '\r') {
                res.append(s, from, i - from);
                res.append('\n');
                if (i + 1 < end && s[i + 1] == '\n') {
                    i++;
                }

                from = i + 1;
            }
        }

        if (from == 0) {   // no \r!
            return input;
        }

        res.append(s, from, end - from);
        return res.toString();
    }

    /**
     * @deprecated Please inline this method.
     */
    @Deprecated
    public static String convertEOLToCRLF(String input) {
        return input.replaceAll("(\r\n|\r|\n)", "\r\n");
    }

    /**
     * Returns a string consisting of "s", plus enough copies of "pad_ch" on the
     * left hand side to make the length of "s" equal to or greater than len (if
     * "s" is already longer than "len", then "s" is returned).
     */
    public static String padLeft(String s, int len, char pad_ch) {
        if (s.length() >= len) {
            return s;
        } else {
            StringBuilder sb = new StringBuilder();
            int n = len - s.length();
            for (int i = 0; i < n; i++) {
                sb.append(pad_ch);
            }
            sb.append(s);
            return sb.toString();
        }
    }

    /**
     * Returns a string consisting of "s", plus enough copies of "pad_ch" on the
     * right hand side to make the length of "s" equal to or greater than len (if
     * "s" is already longer than "len", then "s" is returned).
     */
    public static String padRight(String s, int len, char pad_ch) {
        if (s.length() >= len) {
            return s;
        } else {
            StringBuilder sb = new StringBuilder();
            int n = len - s.length();
            sb.append(s);
            for (int i = 0; i < n; i++) {
                sb.append(pad_ch);
            }
            return sb.toString();
        }
    }

    /**
     * Returns a string consisting of "s", with each of the first "len" characters
     * replaced by "mask_ch" character.
     */
    public static String maskLeft(String s, int len, char mask_ch) {
        if (len <= 0) {
            return s;
        }
        len = Math.min(len, s.length());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(mask_ch);
        }
        sb.append(s.substring(len));
        return sb.toString();
    }

    /**
     * Returns a string consisting of "s", with each of the last "len" characters
     * replaces by "mask_ch" character.
     */
    public static String maskRight(String s, int len, char mask_ch) {
        if (len <= 0) {
            return s;
        }
        len = Math.min(len, s.length());
        StringBuilder sb = new StringBuilder();
        sb.append(s.substring(0, s.length() - len));
        for (int i = 0; i < len; i++) {
            sb.append(mask_ch);
        }
        return sb.toString();
    }

    private static boolean isHex(char c) {
        return ((c >= '0') && (c <= '9')) ||
                ((c >= 'a') && (c <= 'f')) ||
                ((c >= 'A') && (c <= 'F'));
    }

    private static int hexValue(char c) {
        if ((c >= '0') && (c <= '9')) {
            return (c - '0');
        } else if ((c >= 'a') && (c <= 'f')) {
            return (c - 'a') + 10;
        } else {
            return (c - 'A') + 10;
        }
    }

    /**
     * The old interface to cropBetween - using a single char limit
     */
    public static String cropBetween(String in, char limit) {
        return cropBetween(in, String.valueOf(new char[]{limit}));
    }

    /**
     * This removes characters between maching charLimit chars. For example
     * cropBetween("ab^cd^ef^gh^hi", '^') will return "abefhi" It will consider
     * squences of 2 charLimit as one charLimit in the output
     *
     * @param in    - the string to process
     * @param limit - the limit of the string(s) to remove
     * @return String - the cropped string
     */
    public static String cropBetween(String in, String limit) {
        StringBuilder out = new StringBuilder();
        int lastPos = 0;
        int lenLimit = limit.length();
        boolean modeAdd = true;
        int pos = -1;
        while ((pos = in.indexOf(limit, lastPos)) >= 0) {
            if (modeAdd) {
                out.append(in.substring(lastPos, pos));
            }
            modeAdd = !modeAdd;
            lastPos = pos + lenLimit;
        }

        // add the remainings
        if (modeAdd) {
            out.append(in.substring(lastPos));
        }

        return out.toString();
    }

    /**
     * This converts a String to a list of strings by extracting the substrings
     * between delimiter
     *
     * @param in        - what to process
     * @param delimiter - the delimiting string
     * @param doStrip   - to strip the substrings before adding to the list
     * @return LinkedList
     */
    public static LinkedList<String> string2List(String in, String delimiter,
                                                 boolean doStrip) {
        if (in == null) {
            return null;
        }

        LinkedList<String> out = new LinkedList<String>();
        string2Collection(in, delimiter, doStrip, out);
        return out;
    }

    /**
     * This converts a String to a Set of strings by extracting the substrings
     * between delimiter
     *
     * @param in        - what to process
     * @param delimiter - the delimiting string
     * @param doStrip   - to strip the substrings before adding to the list
     * @return Set
     */
    public static Set string2Set(String in, String delimiter, boolean doStrip) {
        if (in == null) {
            return null;
        }

        HashSet<String> out = new HashSet<String>();
        string2Collection(in, delimiter, doStrip, out);
        return out;
    }

    public static String set2String(Set in, String separator) {
        if (in == null) {
            return null;
        }

        return iterator2String(in.iterator(), separator);
    }

    /**
     * Converts a delimited string to a collection of strings. Substrings between
     * delimiters are extracted from the string and added to a collection that is
     * provided by the caller.
     *
     * @param in         The delimited input string to process
     * @param delimiter  The string delimiting entries in the input string.
     * @param doString   Whether to strip the substrings before adding to the
     *                   collection
     * @param collection The collection to which the strings will be added. If
     *                   <code>null</code>, a new <code>List</code> will be created.
     * @return The collection to which the substrings were added. This is
     * syntactic sugar to allow call chaining.
     */
    public static Collection<String> string2Collection(String in,
                                                       String delimiter,
                                                       boolean doStrip,
                                                       Collection<String> collection) {
        if (in == null) {
            return null;
        }
        if (collection == null) {
            collection = new ArrayList<String>();
        }
        if (delimiter == null || delimiter.length() == 0) {
            collection.add(in);
            return collection;
        }

        int fromIndex = 0;
        int pos;
        while ((pos = in.indexOf(delimiter, fromIndex)) >= 0) {
            String interim = in.substring(fromIndex, pos);
            if (doStrip) {
                interim = strip(interim);
            }
            if (!doStrip || interim.length() > 0) {
                collection.add(interim);
            }

            fromIndex = pos + delimiter.length();
        }

        String interim = in.substring(fromIndex);
        if (doStrip) {
            interim = strip(interim);
        }
        if (!doStrip || interim.length() > 0) {
            collection.add(interim);
        }

        return collection;
    }

    /**
     * Lots of people called list2String when in fact it was implemented as
     * Collection2String. I added Collection2String as a new function and am
     * leaving the list2String function signature here so it can continue to be
     *
     * @deprecated Please use
     * But note that {@code Join} does not consider null elements to be
     * equivalent to the empty string, as this method does.
     */
    @Deprecated
    public static String list2String(
            Collection<?> in, String separator) {
        return collection2String(in, separator);
    }

    /**
     * This concatenates the elements of a collection in a string
     *
     * @param in        - the collection that has to be conatenated
     * @param separator - a string to sepparate the elements from the list
     * @return String
     * @deprecated Please use
     * But note that {@code Join} does not consider null elements to be
     * equivalent to the empty string, as this method does.
     */
    @Deprecated
    public static String collection2String(
            Collection<?> in, String separator) {
        if (in == null) {
            return null;
        }
        return iterator2String(in.iterator(), separator);
    }

    /**
     * @deprecated Please use
     * But note that {@code Join} does not consider null elements to be
     * equivalent to the empty string, as this method does.
     */
    @Deprecated
    public static String iterator2String(
            Iterator<?> it, String separator) {
        if (it == null) {
            return null;
        }

        StringBuilder out = new StringBuilder();
        while (it.hasNext()) {
            if (out.length() > 0) {
                out.append(separator);
            }
            out.append(it.next().toString());
        }

        return out.toString();
    }

    /**
     * This converts a string to a Map. It will first split the string into
     * entries using delimEntry. Then each entry is split into a key and a value
     * using delimKey. By default we strip the keys. Use doStripEntry to strip
     * also the entries
     *
     * @param in           - the string to be processed
     * @param delimEntry   - delimiter for the entries
     * @param delimKey     - delimiter between keys and values
     * @param doStripEntry - strip entries before inserting in the map
     * @return HashMap
     */
    public static HashMap<String, String> string2Map(String in, String delimEntry,
                                                     String delimKey, boolean doStripEntry) {

        if (in == null) {
            return null;
        }

        HashMap<String, String> out = new HashMap<String, String>();

        if (isEmpty(delimEntry) || isEmpty(delimKey)) {
            out.put(strip(in), "");
            return out;
        }

        Iterator<String> it = string2List(in, delimEntry, false).iterator();
        int len = delimKey.length();
        while (it.hasNext()) {
            String entry = it.next();
            int pos = entry.indexOf(delimKey);
            if (pos > 0) {
                String value = entry.substring(pos + len);
                if (doStripEntry) {
                    value = strip(value);
                }
                out.put(strip(entry.substring(0, pos)), value);
            } else {
                out.put(strip(entry), "");
            }
        }

        return out;
    }

    /**
     * This function concatenates the elements of a Map in a string with form
     * "<key1><sepKey><value1><sepEntry>...<keyN><sepKey><valueN>"
     *
     * @param in       - the map to be converted
     * @param sepKey   - the separator to put between key and value
     * @param sepEntry - the separator to put between map entries
     * @return String
     */
    public static <K, V> String map2String(Map<K, V> in, String sepKey,
                                           String sepEntry) {
        if (in == null) {
            return null;
        }

        StringBuilder out = new StringBuilder();
        Iterator<Entry<K, V>> it = in.entrySet().iterator();
        while (it.hasNext()) {
            if (out.length() > 0) {
                out.append(sepEntry);
            }
            Entry<K, V> entry = it.next();
            out.append(entry.getKey() + sepKey + entry.getValue());
        }

        return out.toString();
    }

    /**
     * Given a map, creates and returns a new map in which all keys are the
     * lower-cased version of each key.
     *
     * @param map A map containing String keys to be lowercased
     * @throws IllegalArgumentException if the map contains duplicate string keys
     *                                  after lower casing
     */
    public static <V> Map lowercaseKeys(Map<String, V> map) {
        Map<String, V> result = new HashMap<String, V>(map.size());
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            if (result.containsKey(key.toLowerCase())) {
                throw new IllegalArgumentException(
                        "Duplicate string key in map when lower casing");
            }
            result.put(key.toLowerCase(), map.get(key));
        }
        return result;
    }

    /**
     * Replaces any string of adjacent whitespace characters with the whitespace
     * character " ".
     *
     * @param str the string you want to munge
     * @return String with no more excessive whitespace!
     * @see collapse
     */
    public static String collapseWhitespace(String str) {
        return collapse(str, WHITE_SPACES, " ");
    }

    /**
     * Replaces any string of matched characters with the supplied string.<p>
     * <p>
     * This is a more general version of collapseWhitespace.
     * <p>
     * <pre>
     *   E.g. collapse("hello     world", " ", "::")
     *   will return the following string: "hello::world"
     * </pre>
     *
     * @param str         the string you want to munge
     * @param chars       all of the characters to be considered for munge
     * @param replacement the replacement string
     * @return String munged and replaced string.
     */
    public static String collapse(String str, String chars, String replacement) {
        if (str == null) {
            return null;
        }

        StringBuilder newStr = new StringBuilder();

        boolean prevCharMatched = false;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (chars.indexOf(c) != -1) {
                // this character is matched
                if (prevCharMatched) {
                    // apparently a string of matched chars, so don't append anything
                    // to the string
                    continue;
                }
                prevCharMatched = true;
                newStr.append(replacement);
            } else {
                prevCharMatched = false;
                newStr.append(c);
            }
        }

        return newStr.toString();
    }

    /**
     * Read a String of up to maxLength bytes from an InputStream
     *
     * @param is        input stream
     * @param maxLength max number of bytes to read from "is". If this is -1, we
     *                  read everything.
     * @return String up to maxLength bytes, read from "is"
     */
    public static String stream2String(InputStream is, int maxLength)
            throws IOException {
        byte[] buffer = new byte[4096];
        StringWriter sw = new StringWriter();
        int totalRead = 0;
        int read = 0;

        do {
            sw.write(new String(buffer, 0, read));
            totalRead += read;
            read = is.read(buffer, 0, buffer.length);
        } while (((-1 == maxLength) || (totalRead < maxLength)) && (read != -1));

        return sw.toString();
    }

    /**
     * Parse a list of substrings separated by a given delimiter. The delimiter
     * can also appear in substrings (just double them):
     * <p>
     * parseDelimitedString("this|is", '|') returns ["this","is"]
     * parseDelimitedString("this||is", '|') returns ["this|is"]
     *
     * @param list      String containing delimited substrings
     * @param delimiter Delimiter (anything except ' ' is allowed)
     * @return String[] A String array of parsed substrings
     */
    public static String[] parseDelimitedList(String list,
                                              char delimiter) {
        String delim = "" + delimiter;
        // Append a sentinel of delimiter + space
        // (see comments below for more info)
        StringTokenizer st = new StringTokenizer(list + delim + " ",
                delim,
                true);
        ArrayList<String> v = new ArrayList<String>();
        String lastToken = "";
        String word = "";

        // We keep a sliding window of 2 tokens
        //
        // delimiter : delimiter -> append delimiter to current word
        //                          and clear most recent token
        //                          (so delim : delim : delim will not
        //                          be treated as two escaped delims.)
        //
        // tok : delimiter -> append tok to current word
        //
        // delimiter : tok -> add current word to list, and clear it.
        //                    (We append a sentinel that conforms to this
        //                    pattern to make sure we've pushed every parsed token)
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (lastToken != null) {
                if (tok.equals(delim)) {
                    word = word + lastToken;
                    if (lastToken.equals(delim)) tok = null;
                } else {
                    if (!word.equals("")) v.add(word);
                    word = "";
                }
            }
            lastToken = tok;
        }

        return v.toArray(new String[0]);
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     * <p>
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * Helper function for null and empty string testing.
     *
     * @return true iff s == null or s.equals("");
     */
    public static boolean isEmpty(String s) {
        //return makeSafe(s).length() == 0;
        return s == null || s.length() == 0;
    }

    /**
     * Helper function for null, empty, and whitespace string testing.
     *
     * @return true if s == null or s.equals("") or s contains only whitespace
     * characters.
     */
    public static boolean isEmptyOrWhitespace(String s) {
        s = makeSafe(s);
        for (int i = 0, n = s.length(); i < n; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function for making null strings safe for comparisons, etc.
     *
     * @return (s == null) ? "" : s;
     */
    public static String makeSafe(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * Helper function for making empty strings into a null.
     *
     * @return null if s is zero length.  otherwise, returns s.
     */
    public static String toNullIfEmpty(String s) {
        return (StringUtil.isEmpty(s)) ? null : s;
    }

    /**
     * Helper function for turning empty or whitespace strings into a null.
     *
     * @return null if s is zero length or if s contains only whitespace
     * characters.  otherwise, returns s.
     */
    public static String toNullIfEmptyOrWhitespace(String s) {
        return (StringUtil.isEmptyOrWhitespace(s)) ? null : s;
    }

    /**
     * Serializes a map
     *
     * @param map           A map of String keys to arrays of String values
     * @param keyValueDelim Delimiter between keys and values
     * @param entryDelim    Delimiter between entries
     * @return String A string containing a serialized representation of the
     * contents of the map.
     * <p>
     * e.g. arrayMap2String({"foo":["bar","bar2"],"foo1":["bar1"]}, "=", "&")
     * returns "foo=bar&foo=bar2&foo1=bar1"
     */
    public static String arrayMap2String(Map<String, String[]> map,
                                         String keyValueDelim,
                                         String entryDelim) {
        Set<Entry<String, String[]>> entrySet = map.entrySet();
        Iterator<Entry<String, String[]>> itor = entrySet.iterator();
        StringWriter sw = new StringWriter();
        while (itor.hasNext()) {
            Entry<String, String[]> entry = itor.next();
            String key = entry.getKey();
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                sw.write(entry.getKey() + keyValueDelim + values[i]);
                if (i < values.length - 1) {
                    sw.write(entryDelim);
                }
            }
            if (itor.hasNext()) {
                sw.write(entryDelim);
            }
        }
        return sw.toString();
    }

    /**
     * Compares two strings, guarding against nulls If both Strings are null we
     * return true
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == s2) {
            return true; // Either both the same String, or both null
        }
        if (s1 != null) {
            if (s2 != null) {
                return s1.equals(s2);
            }
        }
        return false;
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == s2) {
            return true; // Either both the same String, or both null
        }
        if (s1 != null) {
            if (s2 != null) {
                return s1.equalsIgnoreCase(s2);
            }
        }
        return false;
    }

    /**
     * Splits s with delimiters in delimiter and returns the last token
     */
    public static String lastToken(String s, String delimiter) {
        String[] parts = split(s, delimiter);
        return (parts.length == 0) ? "" : parts[parts.length - 1];
    }

    /**
     * Determines if a string contains only ascii characters
     */
    public static boolean allAscii(String s) {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            if ((s.charAt(i) & 0xff80) != 0) {
                return false;
            }
        }
        return true;
    }

//  /**
//   * Determines if a string contains what looks like an html character
//   * reference. Useful for deciding whether unescaping is necessary.
//   */
//  public static boolean containsCharRef(String s) {
//    return characterReferencePattern.matcher(s).find();
//  }

    /**
     * Determines if a string is a Hebrew word. A string is considered to be
     * a Hebrew word if {@link #isHebrew(int)} is true for any of its characters.
     */
    public static boolean isHebrew(String s) {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            if (isHebrew(s.codePointAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a character is a Hebrew character.
     */
    public static boolean isHebrew(int codePoint) {
        return Character.UnicodeBlock.HEBREW.equals(
                Character.UnicodeBlock.of(codePoint));
    }

    /**
     * Replaces each non-ascii character in s with its Unicode escape sequence
     * \\uxxxx where xxxx is a hex number. Existing escape sequences won't be
     * affected.
     */
    public static String unicodeEscape(String s) {
        if (allAscii(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            char ch = s.charAt(i);
            if (ch <= 127) {
                sb.append(ch);
            } else {
                sb.append("\\u");
                String hexString = Integer.toHexString(ch);
                // Pad with zeros if necessary
                int numZerosToPad = 4 - hexString.length();
                for (int j = 0; j < numZerosToPad; ++j) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
        }
        return sb.toString();
    }

    /**
     * Returns the approximate display width of the string, measured in units of
     * ascii characters.
     *
     * @see displayWidth(char)
     */
    public static int displayWidth(String s) {
        int width = 0;
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            width += displayWidth(s.charAt(i));
        }
        return width;
    }

    /**
     * Returns the approximate display width of the character, measured
     * in units of ascii characters.
     * <p>
     * This method should err on the side of caution. By default, characters
     * are assumed to have width 2; this covers CJK ideographs, various
     * symbols and miscellaneous weird scripts. Given below are some Unicode
     * ranges for which it seems safe to assume that no character is
     * substantially wider than an ascii character:
     * - Latin, extended Latin, even more extended Latin.
     * - Greek, extended Greek, Cyrillic.
     * - Some symbols (including currency symbols) and punctuation.
     * - Half-width Katakana and Hangul.
     * - Hebrew
     * - Thai
     * Characters in these ranges are given a width of 1.
     * <p>
     * IMPORTANT: this function has an analog in strutil.cc named
     * UnicodeCharWidth, which needs to be updated if you change the
     * implementation here.
     */
    public static int displayWidth(char ch) {
        if (ch <= '\u04f9' ||
                ch == '\u05be' ||
                (ch >= '\u05d0' && ch <= '\u05ea') ||
                ch == '\u05F3' ||
                ch == '\u05f4' ||
                (ch >= '\u0e00' && ch <= '\u0e7f') ||
                (ch >= '\u1e00' && ch <= '\u20af') ||
                (ch >= '\u2100' && ch <= '\u213a') ||
                (ch >= '\uff61' && ch <= '\uffdc')) {
            return 1;
        }
        return 2;
    }

    /**
     * @return a string representation of the given native array.
     */
    public static String toString(float[] iArray) {
        if (iArray == null) {
            return "NULL";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < iArray.length; i++) {
            buffer.append(iArray[i]);
            if (i != (iArray.length - 1)) {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @return a string representation of the given native array.
     */
    public static String toString(long[] iArray) {
        if (iArray == null) {
            return "NULL";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < iArray.length; i++) {
            buffer.append(iArray[i]);
            if (i != (iArray.length - 1)) {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @return a string representation of the given native array
     */
    public static String toString(int[] iArray) {
        if (iArray == null) {
            return "NULL";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < iArray.length; i++) {
            buffer.append(iArray[i]);
            if (i != (iArray.length - 1)) {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @return a string representation of the given array.
     */
    public static String toString(String[] iArray) {
        if (iArray == null) return "NULL";

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < iArray.length; i++) {
            buffer.append("'").append(iArray[i]).append("'");
            if (i != iArray.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append("]");

        return buffer.toString();
    }

    /**
     * Returns the string, in single quotes, or "NULL". Intended only for
     * logging.
     *
     * @param s - the string
     * @return the string, in single quotes, or the string "null" if it's null.
     */
    public static String toString(String s) {
        if (s == null) {
            return "NULL";
        } else {
            return new StringBuilder(s.length() + 2).append("'").append(s)
                    .append("'").toString();
        }
    }


    /**
     * @return a string representation of the given native array
     */
    public static String toString(int[][] iArray) {
        if (iArray == null) {
            return "NULL";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < iArray.length; i++) {
            buffer.append("[");
            for (int j = 0; j < iArray[i].length; j++) {
                buffer.append(iArray[i][j]);
                if (j != (iArray[i].length - 1)) {
                    buffer.append(", ");
                }
            }
            buffer.append("]");
            if (i != iArray.length - 1) {
                buffer.append(" ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @return a string representation of the given native array.
     */
    public static String toString(long[][] iArray) {
        if (iArray == null) return "NULL";

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < iArray.length; i++) {
            buffer.append("[");
            for (int j = 0; j < iArray[i].length; j++) {
                buffer.append(iArray[i][j]);
                if (j != (iArray[i].length - 1)) {
                    buffer.append(", ");
                }
            }
            buffer.append("]");
            if (i != iArray.length - 1) {
                buffer.append(" ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @return a String representation of the given object array.
     * The strings are obtained by calling toString() on the
     * underlying objects.
     */
    public static String toString(Object[] obj) {
        if (obj == null) return "NULL";
        StringBuilder tmp = new StringBuilder();
        tmp.append("[");
        for (int i = 0; i < obj.length; i++) {
            tmp.append(obj[i].toString());
            if (i != obj.length - 1) {
                tmp.append(",");
            }
        }
        tmp.append("]");
        return tmp.toString();
    }

    /**
     * Replacement for deprecated StringBufferInputStream(). Instead of:
     * InputStream is = new StringBuilderInputStream(str);
     * do:
     * InputStream is = StringUtil.toUTF8InputStream(str);
     */
    public static InputStream toUTF8InputStream(String str) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // UTF-8 should always be supported
            throw new AssertionError();
        }
        return is;
    }

    /**
     * Copy all data from in to out in 4096 byte chunks.
     */
    public static void copyStreams(InputStream in, OutputStream out)
            throws IOException {
        if (in == null || out == null) {
            throw new IllegalArgumentException();
        }
        final byte[] buffer = new byte[4096];
        int len;
        while (-1 != (len = in.read(buffer, 0, buffer.length))) {
            out.write(buffer, 0, len);
        }
    }

    /**
     * Convert a byte array to a String using Latin-1 (aka ISO-8859-1) encoding.
     * <p>
     * Note: something is probably wrong if you're using this method. Either
     * you're dealing with legacy code that doesn't support i18n or you're
     * using a third-party library that only deals with Latin-1. New code
     * should (almost) always uses UTF-8 encoding.
     *
     * @return the decoded String or null if ba is null
     */
    public static String bytesToLatin1(final byte[] ba) {
        // ISO-8859-1 should always be supported
        return bytesToEncoding(ba, "ISO-8859-1");
    }

    private static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Convert a byte array to a hex-encoding string: "a33bff00..."
     */
    public static String bytesToHexString(final byte[] bytes) {
        return bytesToHexString(bytes, null);
    }

    /**
     * Convert a byte array to a hex-encoding string with the specified
     * delimiter: "a3&lt;delimiter&gt;3b&lt;delimiter&gt;ff..."
     */
    public static String bytesToHexString(final byte[] bytes,
                                          Character delimiter) {
        StringBuffer hex =
                new StringBuffer(bytes.length * (delimiter == null ? 2 : 3));
        int nibble1, nibble2;
        for (int i = 0; i < bytes.length; i++) {
            nibble1 = (bytes[i] >>> 4) & 0xf;
            nibble2 = bytes[i] & 0xf;
            if (i > 0 && delimiter != null) hex.append(delimiter.charValue());
            hex.append(hexChars[nibble1]);
            hex.append(hexChars[nibble2]);
        }
        return hex.toString();
    }

    /**
     * Convert a String to a byte array using Latin-1 (aka ISO-8859-1) encoding.
     * If any character in the String is not Latin-1 (meaning it's high 8 bits
     * are not all zero), then the returned byte array will contain garbage.
     * Therefore, only use this if you know all your characters are within
     * Latin-1.
     * <p>
     * Note: something is probably wrong if you're using this method. Either
     * you're dealing with legacy code that doesn't support i18n or you're
     * using a third-party library that only deals with Latin-1. New code
     * should (almost) always uses UTF-8 encoding.
     *
     * @return the encoded byte array or null if str is null
     */
    public static byte[] latin1ToBytes(final String str) {
        // ISO-8859-1 should always be supported
        return encodingToBytes(str, "ISO-8859-1");
    }

    /**
     * Convert a byte array to a String using UTF-8 encoding.
     *
     * @return the decoded String or null if ba is null
     */
    public static String bytesToUtf8(final byte[] ba) {
        // UTF-8 should always be supported
        return bytesToEncoding(ba, "UTF8");
    }

    /**
     * Convert a String to a byte array using UTF-8 encoding.
     *
     * @return the encoded byte array or null if str is null
     */
    public static byte[] utf8ToBytes(final String str) {
        // UTF-8 should always be supported
        return encodingToBytes(str, "UTF8");
    }

    /**
     * Convert a byte array to a String using the specified encoding.
     *
     * @param encoding the encoding to use
     * @return the decoded String or null if ba is null
     */
    private static String bytesToEncoding(final byte[] ba, final String encoding) {
        if (ba == null) {
            return null;
        }

        try {
            return new String(ba, encoding);
        } catch (final UnsupportedEncodingException e) {
            throw new Error(encoding + " not supported! Original exception: " + e);
        }
    }

    /**
     * Convert a String to a byte array using the specified encoding.
     *
     * @param encoding the encoding to use
     * @return the encoded byte array or null if str is null
     */
    public static byte[] encodingToBytes(
            final String str, final String encoding) {

        if (str == null) {
            return null;
        }

        try {
            return str.getBytes(encoding);
        } catch (final UnsupportedEncodingException e) {
            throw new Error(encoding + " not supported! Original exception: " + e);
        }
    }

    /**
     * Convert an array of bytes into a List of Strings using UTF-8. A line is
     * considered to be terminated by any one of a line feed ('\n'), a carriage
     * return ('\r'), or a carriage return followed immediately by a linefeed.<p/>
     * <p>
     * Can be used to parse the output of
     *
     * @param bytes the array to convert
     * @return A new mutable list containing the Strings in the input array. The
     * list will be empty if bytes is empty or if it is null.
     */
    public static List<String> bytesToStringList(byte[] bytes) {
        List<String> lines = new ArrayList<String>();

        if (bytes == null) {
            return lines;
        }

        BufferedReader r = null;

        try {
            r = new BufferedReader(
                    new InputStreamReader(
                            new ByteArrayInputStream(bytes),
                            "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // If UTF-8 is not supported we are in big trouble.
            throw new RuntimeException(e);
        }

        try {
            try {
                for (String line = r.readLine(); line != null; line = r.readLine()) {
                    lines.add(line);
                }
            } finally {
                r.close();
            }
        } catch (IOException e) {
            // I can't think of a reason we'd get here.
            throw new RuntimeException(e);
        }

        return lines;
    }

    /**
     * Safely convert the string to uppercase.
     *
     * @return upper case representation of the String; or null if
     * the input string is null.
     */
    public static String toUpperCase(String src) {
        if (src == null) {
            return null;
        } else {
            return src.toUpperCase();
        }
    }

//  /**
//   * Returns sourceString concatenated together 'factor' times.
//   *
//   * @param sourceString The string to repeat
//   * @param factor  The number of times to repeat it.
//   */
//  public static String repeat(String sourceString, int factor) {
//    if (factor < 1) {
//      return "";
//    }
//    if (factor == 1) {
//      return sourceString;
//    }
//
//    StringBuilder sb = new StringBuilder(factor * sourceString.length());
//
//    while (factor > 0) {
//      sb.append(sourceString);
//      factor--;
//    }
//
//    return sb.toString();
//  }

    /**
     * Returns a string that is equivalent to the specified string with its
     * first character converted to uppercase as by {@link String#toUpperCase}.
     * The returned string will have the same value as the specified string if
     * its first character is non-alphabetic, if its first character is already
     * uppercase, or if the specified string is of length 0.
     * <p>
     * <p>For example:
     * <pre>
     *    capitalize("foo bar").equals("Foo bar");
     *    capitalize("2b or not 2b").equals("2b or not 2b")
     *    capitalize("Foo bar").equals("Foo bar");
     *    capitalize("").equals("");
     * </pre>
     *
     * @param s the string whose first character is to be uppercased
     * @return a string equivalent to <tt>s</tt> with its first character
     * converted to uppercase
     * @throws NullPointerException if <tt>s</tt> is null
     */
    public static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        char first = s.charAt(0);
        char capitalized = Character.toUpperCase(first);
        return (first == capitalized)
                ? s
                : capitalized + s.substring(1);
    }

    //Contains
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if String contains a search character, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.</p>
     * <p>
     * <p>A <code>null</code> or empty ("") String will return <code>false</code>.</p>
     * <p>
     * <pre>
     * StringUtil.contains(null, *)    = false
     * StringUtil.contains("", *)      = false
     * StringUtil.contains("abc", 'a') = true
     * StringUtil.contains("abc", 'z') = false
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     * @return true if the String contains the search character,
     * false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, char searchChar) {
        if (isEmpty(str)) {
            return false;
        }
        return str.indexOf(searchChar) >= 0;
    }

    /**
     * <p>Checks if String contains a search String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String)}.</p>
     * <p>
     * <p>A <code>null</code> String will return <code>false</code>.</p>
     * <p>
     * <pre>
     * StringUtil.contains(null, *)     = false
     * StringUtil.contains(*, null)     = false
     * StringUtil.contains("", "")      = true
     * StringUtil.contains("abc", "")   = true
     * StringUtil.contains("abc", "a")  = true
     * StringUtil.contains("abc", "z")  = false
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return true if the String contains the search String,
     * false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr) >= 0;
    }

    /**
     * <p>Checks if String contains a search String irrespective of case,
     * handling <code>null</code>. This method uses
     * {@link #contains(String, String)}.</p>
     * <p>
     * <p>A <code>null</code> String will return <code>false</code>.</p>
     * <p>
     * <pre>
     * StringUtil.contains(null, *) = false
     * StringUtil.contains(*, null) = false
     * StringUtil.contains("", "") = true
     * StringUtil.contains("abc", "") = true
     * StringUtil.contains("abc", "a") = true
     * StringUtil.contains("abc", "z") = false
     * StringUtil.contains("abc", "A") = true
     * StringUtil.contains("abc", "Z") = false
     * </pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return true if the String contains the search String irrespective of
     * case or false if not or <code>null</code> string input
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return contains(str.toUpperCase(), searchStr.toUpperCase());
    }

    //Reversing
    //-----------------------------------------------------------------------

    /**
     * <p>Reverses a String as per {@link StringBuffer#reverse()}.</p>
     * <p>
     * <p>A <code>null</code> String returns <code>null</code>.</p>
     * <p>
     * <pre>
     * StringUtil.reverse(null)  = null
     * StringUtil.reverse("")    = ""
     * StringUtil.reverse("bat") = "tab"
     * </pre>
     *
     * @param str the String to reverse, may be null
     * @return the reversed String, <code>null</code> if null String input
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    //Abbreviating
    //-----------------------------------------------------------------------

    /**
     * <p>Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "Now is the time for..."</p>
     * <p>
     * <p>Specifically:
     * <ul>
     * <li>If <code>str</code> is less than <code>maxWidth</code> characters
     * long, return it.</li>
     * <li>Else abbreviate it to <code>(substring(str, 0, max-3) + "...")</code>.</li>
     * <li>If <code>maxWidth</code> is less than <code>4</code>, throw an
     * <code>IllegalArgumentException</code>.</li>
     * <li>In no case will it return a String of length greater than
     * <code>maxWidth</code>.</li>
     * </ul>
     * </p>
     * <p>
     * <pre>
     * StringUtil.abbreviate(null, *)      = null
     * StringUtil.abbreviate("", 4)        = ""
     * StringUtil.abbreviate("abcdefg", 6) = "abc..."
     * StringUtil.abbreviate("abcdefg", 7) = "abcdefg"
     * StringUtil.abbreviate("abcdefg", 8) = "abcdefg"
     * StringUtil.abbreviate("abcdefg", 4) = "a..."
     * StringUtil.abbreviate("abcdefg", 3) = IllegalArgumentException
     * </pre>
     *
     * @param str      the String to check, may be null
     * @param maxWidth maximum length of result String, must be at least 4
     * @return abbreviated String, <code>null</code> if null String input
     * @throws IllegalArgumentException if the width is too small
     * @since 2.0
     */
    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    /**
     * <p>Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "...is the time for..."</p>
     * <p>
     * <p>Works like <code>abbreviate(String, int)</code>, but allows you to specify
     * a "left edge" offset.  Note that this left edge is not necessarily going to
     * be the leftmost character in the result, or the first character following the
     * ellipses, but it will appear somewhere in the result.
     * <p>
     * <p>In no case will it return a String of length greater than
     * <code>maxWidth</code>.</p>
     * <p>
     * <pre>
     * StringUtil.abbreviate(null, *, *)                = null
     * StringUtil.abbreviate("", 0, 4)                  = ""
     * StringUtil.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
     * StringUtil.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
     * StringUtil.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
     * StringUtil.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
     * StringUtil.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
     * StringUtil.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
     * StringUtil.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
     * StringUtil.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
     * StringUtil.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
     * StringUtil.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
     * StringUtil.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
     * </pre>
     *
     * @param str      the String to check, may be null
     * @param offset   left edge of source String
     * @param maxWidth maximum length of result String, must be at least 4
     * @return abbreviated String, <code>null</code> if null String input
     * @throws IllegalArgumentException if the width is too small
     * @since 2.0
     */
    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if ((str.length() - offset) < (maxWidth - 3)) {
            offset = str.length() - (maxWidth - 3);
        }
        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + "...";
        }
        if (maxWidth < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if ((offset + (maxWidth - 3)) < str.length()) {
            return "..." + abbreviate(str.substring(offset), maxWidth - 3);
        }
        return "..." + str.substring(str.length() - (maxWidth - 3));
    }

    public static List removeEmpty(List list) {
        List replace = new ArrayList();
        Object obj;
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            obj = iter.next();
            if (obj != null) {
                replace.add(obj);
            }
        }

        return replace;
    }

    //Padding
    //-----------------------------------------------------------------------

    /**
     * <p>Repeat a String {@code repeat} times to form a
     * new String.</p>
     * <p>
     * <pre>
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = ""
     * </pre>
     *
     * @param str    the String to repeat, may be null
     * @param repeat number of times to repeat str, negative treated as zero
     * @return a new String consisting of the original String repeated,
     * {@code null} if null String input
     */
    public static String repeat(final String str, final int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY_STRING;
        }
        final int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(str.charAt(0), repeat);
        }

        final int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                return repeat(str.charAt(0), repeat);
            case 2:
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                final StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    /**
     * <p>Repeat a String {@code repeat} times to form a
     * new String, with a String separator injected each time. </p>
     * <p>
     * <pre>
     * StringUtils.repeat(null, null, 2) = null
     * StringUtils.repeat(null, "x", 2)  = null
     * StringUtils.repeat("", null, 0)   = ""
     * StringUtils.repeat("", "", 2)     = ""
     * StringUtils.repeat("", "x", 3)    = "xxx"
     * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
     * </pre>
     *
     * @param str       the String to repeat, may be null
     * @param separator the String to inject, may be null
     * @param repeat    number of times to repeat str, negative treated as zero
     * @return a new String consisting of the original String repeated,
     * {@code null} if null String input
     * @since 2.5
     */
    public static String repeat(final String str, final String separator, final int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        }
        // given that repeat(String, int) is quite optimized, better to rely on it than try and splice this into it
        final String result = repeat(str + separator, repeat);
        return removeEnd(result, separator);
    }

    /**
     * <p>Returns padding using the specified delimiter repeated
     * to a given length.</p>
     * <p>
     * <pre>
     * StringUtils.repeat('e', 0)  = ""
     * StringUtils.repeat('e', 3)  = "eee"
     * StringUtils.repeat('e', -2) = ""
     * </pre>
     * <p>
     * <p>Note: this method doesn't not support padding with
     * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
     * as they require a pair of {@code char}s to be represented.
     * If you are needing to support full I18N of your applications
     * consider using {@link #repeat(String, int)} instead.
     * </p>
     *
     * @param ch     character to repeat
     * @param repeat number of times to repeat char, negative treated as zero
     * @return String with repeated character
     * @see #repeat(String, int)
     */
    public static String repeat(final char ch, final int repeat) {
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * <p>Right pad a String with spaces (' ').</p>
     * <p>
     * <p>The String is padded to the size of {@code size}.</p>
     * <p>
     * <pre>
     * StringUtils.rightPad(null, *)   = null
     * StringUtils.rightPad("", 3)     = "   "
     * StringUtils.rightPad("bat", 3)  = "bat"
     * StringUtils.rightPad("bat", 5)  = "bat  "
     * StringUtils.rightPad("bat", 1)  = "bat"
     * StringUtils.rightPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size the size to pad to
     * @return right padded String or original String if no padding is necessary,
     * {@code null} if null String input
     */
    public static String rightPad(final String str, final int size) {
        return rightPad(str, size, ' ');
    }

    /**
     * <p>Right pad a String with a specified character.</p>
     * <p>
     * <p>The String is padded to the size of {@code size}.</p>
     * <p>
     * <pre>
     * StringUtils.rightPad(null, *, *)     = null
     * StringUtils.rightPad("", 3, 'z')     = "zzz"
     * StringUtils.rightPad("bat", 3, 'z')  = "bat"
     * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
     * StringUtils.rightPad("bat", 1, 'z')  = "bat"
     * StringUtils.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return right padded String or original String if no padding is necessary,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String rightPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    /**
     * <p>Right pad a String with a specified String.</p>
     * <p>
     * <p>The String is padded to the size of {@code size}.</p>
     * <p>
     * <pre>
     * StringUtils.rightPad(null, *, *)      = null
     * StringUtils.rightPad("", 3, "z")      = "zzz"
     * StringUtils.rightPad("bat", 3, "yz")  = "bat"
     * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
     * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StringUtils.rightPad("bat", 1, "yz")  = "bat"
     * StringUtils.rightPad("bat", -1, "yz") = "bat"
     * StringUtils.rightPad("bat", 5, null)  = "bat  "
     * StringUtils.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return right padded String or original String if no padding is necessary,
     * {@code null} if null String input
     */
    public static String rightPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            final char[] padding = new char[pads];
            final char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * <p>Left pad a String with spaces (' ').</p>
     * <p>
     * <p>The String is padded to the size of {@code size}.</p>
     * <p>
     * <pre>
     * StringUtils.leftPad(null, *)   = null
     * StringUtils.leftPad("", 3)     = "   "
     * StringUtils.leftPad("bat", 3)  = "bat"
     * StringUtils.leftPad("bat", 5)  = "  bat"
     * StringUtils.leftPad("bat", 1)  = "bat"
     * StringUtils.leftPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size the size to pad to
     * @return left padded String or original String if no padding is necessary,
     * {@code null} if null String input
     */
    public static String leftPad(final String str, final int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * <p>Left pad a String with a specified character.</p>
     * <p>
     * <p>Pad to a size of {@code size}.</p>
     * <p>
     * <pre>
     * StringUtils.leftPad(null, *, *)     = null
     * StringUtils.leftPad("", 3, 'z')     = "zzz"
     * StringUtils.leftPad("bat", 3, 'z')  = "bat"
     * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
     * StringUtils.leftPad("bat", 1, 'z')  = "bat"
     * StringUtils.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return left padded String or original String if no padding is necessary,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    /**
     * <p>Left pad a String with a specified String.</p>
     * <p>
     * <p>Pad to a size of {@code size}.</p>
     * <p>
     * <pre>
     * StringUtils.leftPad(null, *, *)      = null
     * StringUtils.leftPad("", 3, "z")      = "zzz"
     * StringUtils.leftPad("bat", 3, "yz")  = "bat"
     * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
     * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
     * StringUtils.leftPad("bat", 1, "yz")  = "bat"
     * StringUtils.leftPad("bat", -1, "yz") = "bat"
     * StringUtils.leftPad("bat", 5, null)  = "  bat"
     * StringUtils.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary,
     * {@code null} if null String input
     */
    public static String leftPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            final char[] padding = new char[pads];
            final char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * <p>Removes a substring only if it is at the end of a source string,
     * otherwise returns the source string.</p>
     * <p>
     * <p>A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string.
     * A {@code null} search string will return the source string.</p>
     * <p>
     * <pre>
     * StringUtils.removeEnd(null, *)      = null
     * StringUtils.removeEnd("", *)        = ""
     * StringUtils.removeEnd(*, null)      = *
     * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeEnd("abc", "")    = "abc"
     * </pre>
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     * @return the substring with the string removed if found,
     * {@code null} if null String input
     * @since 2.1
     */
    public static String removeEnd(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * 判断是不是URL
     *
     * @param string
     * @return
     */
//    public static boolean isUrl(String string) {
//        Pattern httpPattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\/])+$");
//        return httpPattern.matcher(string).matches();
//    }
}
