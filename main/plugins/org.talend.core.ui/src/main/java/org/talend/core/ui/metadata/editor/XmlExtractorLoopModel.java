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
package org.talend.core.ui.metadata.editor;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;

/**
 * DOC cantoine class global comment. Detailled comment <br/>
 * 
 * $Id: XmlExtractorSchemaModel.java 949 2006-12-11 15:03:40Z cantoine $
 * 
 */
public class XmlExtractorLoopModel extends ExtendedTableModel<XmlXPathLoopDescriptor> {

    private XmlXPathLoopDescriptor xmlXPathLoopDescriptor;

    public XmlExtractorLoopModel(String name) {
        super(name);
    }

    public XmlExtractorLoopModel(XmlXPathLoopDescriptor xmlXPathLoopDescriptor, String name) {
        super(name);
        setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor);
    }

    public XmlXPathLoopDescriptor getXmlXPathLoopDescriptor() {
        return this.xmlXPathLoopDescriptor;
    }

    /**
     * set XmlXPathLoopDescriptor.
     * 
     * @param xmlXPathLoopDescriptor
     */
    public void setXmlXPathLoopDescriptor(XmlXPathLoopDescriptor xmlXPathLoopDescriptor) {
        List<XmlXPathLoopDescriptor> list = new ArrayList<XmlXPathLoopDescriptor>();
        if (xmlXPathLoopDescriptor != null) {
            this.xmlXPathLoopDescriptor = xmlXPathLoopDescriptor;
            list.add(xmlXPathLoopDescriptor);
            registerDataList(list);
        } else {
            list.add(createXmlXPathLoopDescriptor());
            registerDataList(list);
        }
    }

    /**
     * DOC amaumont Comment method "createSchemaTarget".
     * 
     * @return
     */
    public XmlXPathLoopDescriptor createXmlXPathLoopDescriptor() {
        return ConnectionFactory.eINSTANCE.createXmlXPathLoopDescriptor();
    }

}
