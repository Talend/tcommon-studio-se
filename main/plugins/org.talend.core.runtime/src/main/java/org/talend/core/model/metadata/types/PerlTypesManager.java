// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata.types;

import java.util.HashMap;
import java.util.Map;

import org.talend.core.model.metadata.MetadataTalendType;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * <p>
 * 
 * This class was used to convert from old types to new ones. <br>
 * 
 * Generally, It was called when the project and other relationship (<i>such as: job design, schema, context,etc</i>)
 * were created on old version.
 */
public class PerlTypesManager {

    public static final String BOOLEAN = "boolean"; //$NON-NLS-1$

    public static final String DATE = "date"; //$NON-NLS-1$

    public static final String DATETIME = "datetime"; //$NON-NLS-1$

    public static final String INT = "int"; //$NON-NLS-1$

    public static final String DECIMAL = "decimal"; //$NON-NLS-1$

    public static final String STRING = "string"; //$NON-NLS-1$

    private static Map<String, String> perlOldmappingnew;

    private static final String[] PERL_TYPES = MetadataTalendType.getPerlTypes();

    // Default TalendTypes, also be old types of Perl.
    private static final String[] OLD_PERL_TYPES = MetadataTalendType.loadTalendTypes("TALENDDEFAULT", false); //$NON-NLS-1$

    private static boolean initDone = false;

    /**
     * 
     * DOC ggu PerlTypesManager constructor comment.
     */
    private PerlTypesManager() {

    }

    /**
     * 
     * DOC ggu Comment method "initializePerlTypes".
     */
    public static void initializePerlTypes() {
        if (!initDone) {
            perlOldmappingnew = new HashMap<String, String>();

            // old types
            perlOldmappingnew.put(OLD_PERL_TYPES[0], PERL_TYPES[5]); // char to string
            perlOldmappingnew.put(OLD_PERL_TYPES[1], PERL_TYPES[1]); // Day to date
            perlOldmappingnew.put(OLD_PERL_TYPES[2], PERL_TYPES[4]); // double to decimal
            perlOldmappingnew.put(OLD_PERL_TYPES[3], PERL_TYPES[4]); // float to decimal
            perlOldmappingnew.put(OLD_PERL_TYPES[5], PERL_TYPES[3]); // long to int
            perlOldmappingnew.put(OLD_PERL_TYPES[6], PERL_TYPES[5]); // String to string

            perlOldmappingnew.put(OLD_PERL_TYPES[4], PERL_TYPES[3]); // int to int

            // new types
            perlOldmappingnew.put(PERL_TYPES[0], PERL_TYPES[0]); // boolean
            perlOldmappingnew.put(PERL_TYPES[1], PERL_TYPES[1]); // date
            perlOldmappingnew.put(PERL_TYPES[2], PERL_TYPES[2]); // datetime
            perlOldmappingnew.put(PERL_TYPES[4], PERL_TYPES[4]); // decimal
            perlOldmappingnew.put(PERL_TYPES[5], PERL_TYPES[5]); // string

            initDone = true;
        }
    }

    /**
     * 
     * DOC ggu Comment method "getTypeNameOldtoNew".
     * 
     * @param oldType
     * @return relationship newType in Perl.
     */
    public static String getNewTypeName(String oldType) {
        initializePerlTypes();
        return perlOldmappingnew.get(oldType);
    }

    public static String[] getPerlTypes() {
        return PERL_TYPES.clone();
    }
}
