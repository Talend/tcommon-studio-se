// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.string;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.talend.utils.sugars.ReturnCode;

/**
 * @author scorreia. A String utility class.
 */
public final class StringUtilities {

    private StringUtilities() {
    }

    /**
     * Method "tokenize".
     *
     * @param input the string to split
     * @param delimiters the delimiter(s) of the fields
     * @see StringTokenizer
     * @return a list of Strings or an empty array when a null argument is given or when there is no string to split
     */
    public static List<String> tokenize(String input, String delimiters) {
        List<String> stringArray = new ArrayList<String>();

        if (input == null) {
            return stringArray;
        }
        if (delimiters == null) {
            return stringArray;
        }

        StringTokenizer t = new StringTokenizer(input, delimiters);

        while (t.hasMoreTokens()) {
            stringArray.add(t.nextToken());
        }
        return stringArray;
    } // eom tokenize

    /**
     * Method "checkBalancedParenthesis".
     *
     * @param input the string to check
     * @param openingBlock can be a left parenthesis,...
     * @param closingBlock can be a right parenthesis...
     * @return true when the parenthesis are well balanced.
     */
    public static ReturnCode checkBalancedParenthesis(String input, char openingBlock, char closingBlock) {
        int level = 0;
        int i;
        for (i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == openingBlock) {
                level++;
            } else if (currentChar == closingBlock) {
                level--;
            }
            if (level < 0) {
                return new ReturnCode("too many " + closingBlock + " at position " + i, false); //$NON-NLS-1$//$NON-NLS-2$
            }
        }
        if (level < 0) {
            return new ReturnCode("too many " + closingBlock + " at position " + i, false); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (level > 0) {
            return new ReturnCode("too many " + openingBlock + " at position " + i, false); //$NON-NLS-1$//$NON-NLS-2$
        }
        return new ReturnCode();
    }

    /**
     * DOC xqliu Comment method "getRandomString".
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789"; //$NON-NLS-1$
        Random random = new SecureRandom();
        StringBuilder sf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());// str.length() is 62, so 0~61
            sf.append(str.charAt(number));
        }
        return sf.toString();
    }

    public static String removeEndingString(String fullStr, String endingStr) {
        String newStr = fullStr;

        while (0 < newStr.length()) {
            if (newStr.endsWith(endingStr)) {
                newStr = newStr.substring(0, newStr.length() - endingStr.length());
            } else {
                break;
            }
        }

        return newStr;
    }

    public static String removeStartingString(String fullStr, String startingStr) {
        String newStr = fullStr;

        while (0 < newStr.length()) {
            if (newStr.startsWith(startingStr)) {
                newStr = newStr.substring(startingStr.length());
            } else {
                break;
            }
        }

        return newStr;
    }
}
