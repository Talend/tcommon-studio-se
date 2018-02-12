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
package org.talend.core.ui.context.model.template;

import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.model.ContextTabParentModel;

/**
 * DOC Marvin class global comment. Detailled comment
 */
public class ContextVariableTabParentModel extends ContextTabParentModel {

    private IContextParameter contextParameter;

    private String projectLabel;

    private String sourceName;

    public IContextParameter getContextParameter() {
        return this.contextParameter;
    }

    public void setContextParameter(IContextParameter contextParameter) {
        this.contextParameter = contextParameter;
    }

    /**
     * Getter for projectLabel.
     * 
     * @return the projectLabel
     */
    public String getProjectLabel() {
        return this.projectLabel;
    }

    /**
     * Sets the projectLabel.
     * 
     * @param projectLabel the projectLabel to set
     */
    public void setProjectLabel(String projectLabel) {
        this.projectLabel = projectLabel;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

}
