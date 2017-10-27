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
package org.talend.core.ui.context.model.table;

import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.model.ContextTabParentModel;

/**
 * 
 */
public class ContextTableTabParentModel extends ContextTabParentModel {

    private String projectLabel;

    // This is used to store the source name not variable name.
    private String sourceName;

    protected IContextParameter contextParameter;

    private int order;

    /**
     */
    public ContextTableTabParentModel() {
        super();
    }

    /**
     * @param sourceId
     * @param name
     */
    public ContextTableTabParentModel(String sourceId, String name) {
        super(sourceId, name);
    }

    public String getProjectLabel() {
        return this.projectLabel;
    }

    public void setProjectLabel(String projectLabel) {
        this.projectLabel = projectLabel;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public IContextParameter getContextParameter() {
        return this.contextParameter;
    }

    public void setContextParameter(IContextParameter contextParameter) {
        this.contextParameter = contextParameter;
    }

    /**
     * Getter for order.
     * 
     * @return the order
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Sets the order.
     * 
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
