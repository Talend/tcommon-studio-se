// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils;

import java.util.Random;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class IdGenerator {

    /**
     * getAsciiRandomString : Return a randomly generated String
     * 
     * {talendTypes} String
     * 
     * {param} int(6) length: length of the String to return
     * 
     * {example} getAsciiRandomString(6) # Art34Z
     */
    public static String getAsciiRandomString(int length) {
        Random random = new Random();
        int cnt = 0;
        StringBuffer buffer = new StringBuffer();
        char ch;
        int end = 'z' + 1;
        int start = ' ';
        while (cnt < length) {
            ch = (char) (random.nextInt(end - start) + start);
            if (Character.isLetterOrDigit(ch)) {
                buffer.append(ch);
                cnt++;
            }
        }
        return buffer.toString();
    }

}
