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
package org.talend.core.model.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: XmlRow.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class XmlRow {

    private List<XmlField> fields;

    /**
     * Constructs a new XmlRow.
     */
    public XmlRow() {
        super();
        fields = new ArrayList<XmlField>();
    }

    /**
     * Getter for fields.
     * 
     * @return the fields
     */
    public List<XmlField> getFields() {
        return this.fields;
    }

    /**
     * Adds a fields at the end of the row.
     * 
     * @param field Field to add.
     */
    public void add(XmlField field) {
        fields.add(field);
    }

}
