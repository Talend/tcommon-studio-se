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
package org.talend.commons.utils.data.text;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class StringHelper {

    private static final String OPEN_BRACE = "{"; //$NON-NLS-1$

    private static final String CLOSE_BRACE = "}"; //$NON-NLS-1$

    /**
     * Replace MessageFormat.format(..) because MessageFormat does'nt support single quote correctly This method replace
     * in the text all the patterns {0}, {1}, etc.. by all values in arguments array One or more values can be null, no
     * exception is thrown if a value is null.
     * 
     * @param pattern
     * @param arguments
     * @return
     * 
     */
    public static String replacePrms(String text, Object[] arguments) {

        for (int i = 0; i < arguments.length; i++) {
            text = StringUtils.replace(text, OPEN_BRACE + i + CLOSE_BRACE, String.valueOf(arguments[i]));
        }
        return text;
    }

    /**
     * Replace MessageFormat.format(..) because MessageFormat does'nt support single quote correctly This method replace
     * in the text all the patterns {0}, {1}, etc.. by all values in arguments array One or more values can be null, no
     * exception is thrown if a value is null.
     * 
     * @param pattern
     * @param arguments
     * @return
     * 
     */
    public static String replacePrms(String text, Object[] arguments, String strBeforeIndex, String strAfterIndex) {

        for (int i = 0; i < arguments.length; i++) {
            text = StringUtils.replace(text, strBeforeIndex + i + strAfterIndex, String.valueOf(arguments[i]));
        }
        return text;
    }

}
