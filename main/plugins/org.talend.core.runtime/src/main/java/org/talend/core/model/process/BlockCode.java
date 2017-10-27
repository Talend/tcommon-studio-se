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
public class BlockCode {

    private String label;

    /**
     * Constructor.
     * 
     * @param label label of block code
     */
    public BlockCode(String label) {
        super();
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
