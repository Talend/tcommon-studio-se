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
package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class TalendTypePreLenRetriever {

    private MappingType mappingtype;

    private int dbLength;

    private int dbPrecion;

    private String talendTypeNew;

    public TalendTypePreLenRetriever(MappingType mappingType, int dbLength, int dbPrecion) {
        this.mappingtype = mappingType;
        this.dbLength = dbLength;
        this.dbPrecion = dbPrecion;
        this.talendTypeNew = mappingType.getTalendType();
        List<TalendTypePrecisionLength> list = new ArrayList();
        TalendTypePrecisionLength talendTypeFloat = new TalendTypePrecisionLength("id_Float", 32, 0, 32, 1); //$NON-NLS-1$
        TalendTypePrecisionLength talendTypeInt = new TalendTypePrecisionLength("id_Integer", 16, 0, 0, 0); //$NON-NLS-1$
        TalendTypePrecisionLength talendTypeLong = new TalendTypePrecisionLength("id_Long", 64, 17, 0, 0); //$NON-NLS-1$
        TalendTypePrecisionLength talendTypeDouble = new TalendTypePrecisionLength("id_Double", 64, 0, 64, 33); //$NON-NLS-1$
        list.add(talendTypeFloat);
        list.add(talendTypeInt);
        list.add(talendTypeDouble);
        list.add(talendTypeLong);
        init(list);
    }

    public void init(List<TalendTypePrecisionLength> talendTypeList) {
        if (mappingtype.getTalendType().equals("id_Float") || mappingtype.getTalendType().equals("id_Integer") //$NON-NLS-1$ //$NON-NLS-2$
                || mappingtype.getTalendType().equals("id_Long") || mappingtype.getTalendType().equals("id_Double")) { //$NON-NLS-1$ //$NON-NLS-2$
            for (TalendTypePrecisionLength talendtype : talendTypeList) {
                if ((talendtype.getLengthMin() <= dbLength && dbLength <= talendtype.getLengthMax())
                        && (talendtype.getPrecMin() <= dbPrecion && dbPrecion <= talendtype.getPrecMax())) {
                    talendTypeNew = talendtype.getTalendtype();
                    return;
                }
            }
        } else {
            return;
        }

    }

    public String getMappingType() {

        return this.talendTypeNew;
    }
}
