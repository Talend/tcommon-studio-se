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

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;

/**
 * DOC wyang class global comment. Detailled comment
 */
public class Java2STLangTypesHelper {

    /**
     * DOC return as: int int64, big_int, float, bool, char, string.
     * 
     * @param metadataTable
     * @param columnLabel
     * @return
     */
    public static String getSTLangType(IMetadataTable metadataTable, String columnLabel) {
        IMetadataColumn column = metadataTable.getColumn(columnLabel);
        String talendType = column.getTalendType();

        String typeToGenerate = JavaTypesManager.getTypeToGenerate(talendType, false);
        // Integers: int(Integer), long(Long), short(Short), byte(Byte), BigDecimal.
        if (typeToGenerate.equals("int") || typeToGenerate.equals("short") || typeToGenerate.equals("byte")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return "int"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("long")) { //$NON-NLS-1$
            return "int64"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("BigDecimal")) { //$NON-NLS-1$
            return "big_int"; //$NON-NLS-1$
        }

        // Floats: float(Float), double(Double)
        if (typeToGenerate.equals("float") || typeToGenerate.equals("double")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "float"; //$NON-NLS-1$
        }

        // Bool: bool(Boolean)
        if (typeToGenerate.equals("bool")) { //$NON-NLS-1$
            return "bool"; //$NON-NLS-1$
        }

        // Characters: char(Character)
        if (typeToGenerate.equals("char")) { //$NON-NLS-1$
            return "string"; //$NON-NLS-1$
        }

        // others treat as string
        return "string"; //$NON-NLS-1$

    }

    /**
     * DOC return as: int, long, double, float, string.
     * 
     * @author rdubois
     * @param metadataTable
     * @param columnLabel
     * @return
     */

    public static String getPigType(IMetadataTable metadataTable, String columnLabel) { // This method returns the Pig
                                                                                        // type according to the
                                                                                        // talendType
        IMetadataColumn column = metadataTable.getColumn(columnLabel);
        String talendType = column.getTalendType();

        String typeToGenerate = JavaTypesManager.getTypeToGenerate(talendType, false);
        // Integers: int(Integer), long(Long), short(Short), byte(Byte), BigDecimal, double(Double).
        if (typeToGenerate.equals("int") || typeToGenerate.equals("short") || typeToGenerate.equals("byte")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return "int"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("long")) { //$NON-NLS-1$
            return "long"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("BigDecimal") || typeToGenerate.equals("double")) { //$NON-NLS-1$
            return "double"; //$NON-NLS-1$
        }

        // Floats: float(Float)
        if (typeToGenerate.equals("float")) { //$NON-NLS-1$ 
            return "float"; //$NON-NLS-1$
        }

        // others treat as string
        return "chararray"; //$NON-NLS-1$

    }

    /**
     * DOC return as: int, long, double, float, string.
     * 
     * @author rdubois
     * @param metadataTable
     * @param columnLabel
     * @return
     */

    public static String getHCatType(IMetadataTable metadataTable, String columnLabel) { // This method returns the Pig
                                                                                         // type according to the
                                                                                         // talendType
        IMetadataColumn column = metadataTable.getColumn(columnLabel);
        String talendType = column.getTalendType();

        String typeToGenerate = JavaTypesManager.getTypeToGenerate(talendType, false);
        // Integers: int(Integer), long(Long), short(Short), byte(Byte), BigDecimal, double(Double).
        if (typeToGenerate.equals("int") || typeToGenerate.equals("byte")) { //$NON-NLS-1$ //$NON-NLS-2$ 
            return "int"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("long")) { //$NON-NLS-1$
            return "bigint"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("short")) { //$NON-NLS-1$
            return "smallint"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("boolean")) { //$NON-NLS-1$
            return "boolean"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("BigDecimal") || typeToGenerate.equals("double")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "double"; //$NON-NLS-1$
        }

        // Floats: float(Float)
        if (typeToGenerate.equals("float")) { //$NON-NLS-1$ 
            return "float"; //$NON-NLS-1$
        }

        // others treat as string
        return "string"; //$NON-NLS-1$

    }

    /**
     * DOC return as: %d, %f, %b, %s.
     * 
     * @param metadataTable
     * @param columnLabel
     * @return
     */
    public static String getFormatString(IMetadataTable metadataTable, String columnLabel) {
        IMetadataColumn column = metadataTable.getColumn(columnLabel);
        String talendType = column.getTalendType();

        String typeToGenerate = JavaTypesManager.getTypeToGenerate(talendType, false);
        // Integers: int(Integer), long(Long), short(Short), byte(Byte), BigDecimal.
        if (typeToGenerate.equals("int") || typeToGenerate.equals("short") || typeToGenerate.equals("byte")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return "%d"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("long")) { //$NON-NLS-1$
            return "%d"; //$NON-NLS-1$
        } else if (typeToGenerate.equals("BigDecimal")) { //$NON-NLS-1$
            return "%d"; //$NON-NLS-1$
        }

        // Floats: float(Float), double(Double)
        if (typeToGenerate.equals("float") || typeToGenerate.equals("double")) { //$NON-NLS-1$ //$NON-NLS-2$
            return "%f"; //$NON-NLS-1$
        }

        // Bool: bool(Boolean)
        if (typeToGenerate.equals("bool")) { //$NON-NLS-1$
            return "%b"; //$NON-NLS-1$
        }

        // Characters: char(Character)
        if (typeToGenerate.equals("char")) { //$NON-NLS-1$
            return "%c"; //$NON-NLS-1$
        }

        // others treat as string
        return "%s"; //$NON-NLS-1$

    }
}
