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
package org.talend.core.ui.metadata.editor;

import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;

/**
 * DOC cantoine class global comment. Detailled comment <br/>
 * 
 * $Id: XmlExtractorFieldModel.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class XmlExtractorFieldModel extends ExtendedTableModel<SchemaTarget> {

    public XmlExtractorFieldModel(String name) {
        super(name);
    }

    public XmlExtractorFieldModel(List schemaTargetList, String name) {
        super(name);
        setXmlXPathLoopDescriptor(schemaTargetList);
    }

    /**
     * set XmlXPathLoopDescriptor.
     * 
     * @param schemaTargetList
     */
    public void setXmlXPathLoopDescriptor(List schemaTargetList) {
        registerDataList((List<SchemaTarget>) schemaTargetList);
    }

    /**
     * DOC amaumont Comment method "createSchemaTarget".
     * 
     * @return
     */
    public SchemaTarget createNewSchemaTarget() {
        return ConnectionFactory.eINSTANCE.createSchemaTarget();
    }

}
