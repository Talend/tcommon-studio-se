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

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: XmlField.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class XmlField {

    /** Field value. */
    private String value;

    /**
     * Constructs a new XmlField.
     */
    public XmlField(String value) {
        super();

        this.value = value;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

}
