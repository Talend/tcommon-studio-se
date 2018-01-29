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
package org.talend.repository.mdm.model;

import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMXSDExtractorFieldModel extends ExtendedTableModel<ConceptTarget> {

    public MDMXSDExtractorFieldModel(String name) {
        super(name);
    }

    public MDMXSDExtractorFieldModel(List schemaTargetList, String name) {
        super(name);
        setConcept(schemaTargetList);
    }

    /**
     * set XmlXPathLoopDescriptor.
     * 
     * @param schemaTargetList
     */
    public void setConcept(List schemaTargetList) {
        registerDataList((List<ConceptTarget>) schemaTargetList);
    }

    /**
     * DOC amaumont Comment method "createSchemaTarget".
     * 
     * @return
     */
    public ConceptTarget createNewSchemaTarget() {
        return ConnectionFactory.eINSTANCE.createConceptTarget();
    }

}
