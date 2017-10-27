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
package org.talend.metadata.managment.utils;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ManagementTextUtils {

    public static final String QUOTATION_MARK = "\""; //$NON-NLS-1$

    public static String filterSpecialChar(String input) { // for 8115
        if (input != null && !"".equals(input)) {
            for (int i = 0; i < input.length();) {
                char ch = input.charAt(i);
                switch (ch) {
                case 0x0:
                case 0x1:
                case 0x2:
                case 0x3:
                case 0x4:
                case 0x5:
                case 0x6:
                case 0x7:
                case 0x8:
                case 0x9:
                case 0xB:
                case 0xC:
                case 0xE:
                case 0xF:
                case 0x10:
                case 0x11:
                case 0x12:
                case 0x13:
                case 0x14:
                case 0x15:
                case 0x16:
                case 0x17:
                case 0x18:
                case 0x19:
                case 0x1A:
                case 0x1B:
                case 0x1C:
                case 0x1D:
                case 0x1E:
                case 0x1F:
                    input = input.substring(0, i) + input.substring(i + 1, input.length());
                    break;
                default:
                    i++;

                }
            }
        }

        return input;
    }
}
