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
package org.talend.core.model.process;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class HashableColumn implements IHashableColumn {

    private String name;

    private int index;

    /**
     * DOC amaumont HashableColumn constructor comment.
     * 
     * @param name
     * @param index
     */
    public HashableColumn(String name, int index) {
        super();
        this.name = name;
        this.index = index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashableColumn#getIndex()
     */
    public int getIndex() {
        return this.index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashableColumn#getName()
     */
    public String getName() {
        return this.name;
    }

}
