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
package org.talend.core.ui.context.model;

import org.talend.core.model.process.IContextParameter;

/**
 * Added by Marvin Wang on Mar.7, 2012 for job context view tabs as abstract child model, all the second level node
 * model of tabs like "Variables", "Values as tree" and "Values as table" in job Contexts view should be extended this.
 */
public abstract class ContextTabChildModel {

    private String sourceId;

    private String name;

    private ContextTabParentModel parent;

    protected IContextParameter contextParameter;

    public ContextTabChildModel() {
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IContextParameter getContextParameter() {
        return this.contextParameter;
    }

    public void setContextParameter(IContextParameter contextParameter) {
        this.contextParameter = contextParameter;
    }

    public ContextTabParentModel getParent() {
        return this.parent;
    }

    public void setParent(ContextTabParentModel parent) {
        this.parent = parent;
    }

}
