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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.dnd;

import java.util.ArrayList;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class XmlToSchemaDraggedData {

    private List<TransferableXPathEntry> transferableEntryList = new ArrayList<TransferableXPathEntry>();

    /**
     * @param o
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(TransferableXPathEntry o) {
        return this.transferableEntryList.add(o);
    }

    /**
     * Getter for transferableEntryList.
     * 
     * @return the transferableEntryList
     */
    public List<TransferableXPathEntry> getTransferableEntryList() {
        return this.transferableEntryList;
    }

}
