// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package string;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.talend.utils.string.AsciiUtils;
import org.talend.utils.time.TimeTracer;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class AsciiUtilsTest {

    private static final boolean TRACE = false;

    static Random rg = new Random(12345643512L);

    String generateString(int length) {
        final int maxChar = 256;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            char c = (char) rg.nextInt(maxChar);
            sb.append(c);
        }
        if (TRACE) {
            System.out.println("Generated string = " + sb);
        }
        return sb.toString();
    }

    String[] generateNStrings(int n, int length) {
        String[] strings = new String[n];
        for (int i = 0; i < n; i++) {
            strings[i] = generateString(length);
        }
        return strings;
    }

    /**
     * Test method for {@link org.talend.utils.string.AsciiUtils#unaccent(java.lang.String)}.
     */
    @Test
    public void testUnaccent() {
        int nbStrings = 1000000;
        int nbChars = 150;
        TimeTracer tt = new TimeTracer("unaccent", null);
        String[] generatedStrings = generateNStrings(nbStrings, nbChars);
        tt.start();
        for (String string : generatedStrings) {
            if (TRACE) {
                System.out.println(AsciiUtils.unaccent(string));
            } else {
                AsciiUtils.unaccent(string);
            }
        }
        tt.end("ASCIIUTIL time for removing accents in " + nbStrings + " strings");

        tt.start();
        for (String string : generatedStrings) {
            if (TRACE) {
                System.out.println(removeDiacriticalMark(string));
            } else {
                removeDiacriticalMark(string);
            }
        }
        tt.end("MURAL time for removing accents in " + nbStrings + " strings");

        tt.start();
        for (String string : generatedStrings) {
            if (TRACE) {
                System.out.println(enhremoveDiacriticalMark(string));
            } else {
                enhremoveDiacriticalMark(string);
            }
        }
        tt.end("MURAL ENH time for removing accents in " + nbStrings + " strings");

    }

    /**
     * Test method for
     * {@link org.talend.utils.string.AsciiUtils#replaceCharacters(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @Ignore("is not yet implemeneted")
    public void testReplaceCharacters() {
        fail("Not yet implemented");
    }

    /**
     * Removes diacritical mark from a character
     * 
     * @param ch a character
     * @return the same input character without the diacritical mark if any.
     */
    public static char removeDiacriticalMark(char c) {

        if (c < 192) {
            return c;
        }
        if (c >= 192 && c <= 197) {
            return 'A';
        }
        if (c == 199) {
            return 'C';
        }
        if (c >= 200 && c <= 203) {
            return 'E';
        }
        if (c >= 204 && c <= 207) {
            return 'I';
        }
        if (c == 208) {
            return 'D';
        }
        if (c == 209) {
            return 'N';
        }
        if ((c >= 210 && c <= 214) || c == 216) {
            return 'O';
        }
        if (c >= 217 && c <= 220) {
            return 'U';
        }
        if (c == 221) {
            return 'Y';
        }
        if (c >= 224 && c <= 229) {
            return 'a';
        }
        if (c == 231) {
            return 'c';
        }
        if (c >= 232 && c <= 235) {
            return 'e';
        }
        if (c >= 236 && c <= 239) {
            return 'i';
        }
        if (c == 240) {
            return 'd';
        }
        if (c == 241) {
            return 'n';
        }
        if ((c >= 242 && c <= 246) || c == 248) {
            return 'o';
        }
        if (c >= 249 && c <= 252) {
            return 'u';
        }
        if (c == 253 || c == 255) {
            return 'y';
        }

        return c;
    }

    /**
     * Removes diacritical mark from a string
     * 
     * @param st a string
     * @return the same input string without the diacritical mark if any.
     */
    public static String removeDiacriticalMark(String st) {

        int len = st.length();
        String tempS = new String(st);

        for (int i = 0; i < len; i++) {
            char ch = tempS.charAt(i);
            tempS = tempS.replace(ch, removeDiacriticalMark(ch));
        }
        return tempS;
    }

    /**
     * Removes diacritical mark from a string
     * 
     * @param st a string
     * @return the same input string without the diacritical mark if any.
     */
    public static String enhremoveDiacriticalMark(String st) {

        int len = st.length();
        StringBuffer tempS = new StringBuffer();

        for (int i = 0; i < len; i++) {
            tempS.append(removeDiacriticalMark(st.charAt(i)));
        }
        return tempS.toString();
    }

}
