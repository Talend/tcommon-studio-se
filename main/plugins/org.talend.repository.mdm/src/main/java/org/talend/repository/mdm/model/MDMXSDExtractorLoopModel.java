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
package org.talend.repository.mdm.model;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMXSDExtractorLoopModel extends ExtendedTableModel<Concept> {

    private Concept concept;

    public MDMXSDExtractorLoopModel(String name) {
        super(name);
    }

    public MDMXSDExtractorLoopModel(Concept concept, String name) {
        super(name);
        setConcept(concept);
    }

    public Concept getConcept() {
        return this.concept;
    }

    /**
     * set XmlXPathLoopDescriptor.
     * 
     * @param xmlXPathLoopDescriptor
     */
    public void setConcept(Concept concept) {
        List<Concept> list = new ArrayList<Concept>();
        if (concept != null) {
            this.concept = concept;
            list.add(concept);
            registerDataList(list);
        } else {
            list.add(createConcept());
            registerDataList(list);
        }
    }

    /**
     * DOC amaumont Comment method "createSchemaTarget".
     * 
     * @return
     */
    public Concept createConcept() {
        return ConnectionFactory.eINSTANCE.createConcept();
    }

}
