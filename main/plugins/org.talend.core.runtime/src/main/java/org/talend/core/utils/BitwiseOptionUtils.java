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
package org.talend.core.utils;

/**
 * created by ycbai on 2015年5月15日 Detailled comment
 *
 */
public class BitwiseOptionUtils {

    /**
     * Estimate whether <code>option</code> is contained by <code>options</code>.
     * <p>
     * DOC ycbai Comment method "containOption".
     * 
     * @param options
     * @param option
     * @return
     */
    public static boolean containOption(int options, int option) {
        return (options & option) != 0;
    }

}
