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

import org.talend.core.model.metadata.types.PerlDataTypeHelper;

/**
 * DOC ocarbone class global comment. Detailled comment <br/>
 * 
 * $Id: TestDataTypeHelper.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public final class TestDataTypeHelper {

    /**
     * Default Constructor. Must not be used.
     */
    private TestDataTypeHelper() {
    }

    /**
     * DOC ocarbone Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("empty => " + PerlDataTypeHelper.getTalendTypeOfValue("")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("1 => " + PerlDataTypeHelper.getTalendTypeOfValue("1")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("A => " + PerlDataTypeHelper.getTalendTypeOfValue("A")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("1E4 => " + PerlDataTypeHelper.getTalendTypeOfValue("1E4")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("-2147483646 => " + PerlDataTypeHelper.getTalendTypeOfValue("-2147483646")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("2147483647 => " + PerlDataTypeHelper.getTalendTypeOfValue("2147483647")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("-2147483649 => " + PerlDataTypeHelper.getTalendTypeOfValue("-2147483649")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("9223372036854775807 => " + PerlDataTypeHelper.getTalendTypeOfValue("9223372036854775807")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out
                .println("-9223372036854775809 => " + PerlDataTypeHelper.getTalendTypeOfValue("-9223372036854775809")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("9223372036854775808 => " + PerlDataTypeHelper.getTalendTypeOfValue("9223372036854775808")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("2.3 => " + PerlDataTypeHelper.getTalendTypeOfValue("2.3")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("-2.3 => " + PerlDataTypeHelper.getTalendTypeOfValue("-2.3")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("10002.3.1 => " + PerlDataTypeHelper.getTalendTypeOfValue("10002.3.1")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("ABC3 => " + PerlDataTypeHelper.getTalendTypeOfValue("ABC3")); //$NON-NLS-1$ //$NON-NLS-2$

        System.out.println("__________________________"); //$NON-NLS-1$

        // * float flottant (real) -1.4*10 -45 � 3.4*10 38
        System.out.println("float:" + PerlDataTypeHelper.getTalendTypeOfValue("1.11111111111")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("float:" //$NON-NLS-1$
                + PerlDataTypeHelper.getTalendTypeOfValue("-0.0000000000000000000000000000000000000000000014")); //$NON-NLS-1$
        System.out.println("float:" //$NON-NLS-1$
                + PerlDataTypeHelper.getTalendTypeOfValue("339999999999999999999999999999999999999.9")); // * //$NON-NLS-1$
        // double
        // flottant double 4.9*10 -324 � 1.7*10 308
        System.out
                .println("double:" //$NON-NLS-1$
                        + PerlDataTypeHelper
                                .getTalendTypeOfValue("1699999999999999999999999999999999999999999999999999999999999999999999999999.9")); //$NON-NLS-1$
        System.out
                .println("double:" //$NON-NLS-1$
                        + PerlDataTypeHelper
                                .getTalendTypeOfValue("1700000000000000000000000000000000000000000000000000000000000000000000000000")); //$NON-NLS-1$
    }

}
