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
package org.talend.core.ui.context.model.tree;

import org.talend.core.ui.context.model.ContextTabChildModel;

/**
 * Added by Marvin Wang on Mar.7, 2012 for the tab "Values as tree" view as model.
 */
public class ContextTreeTabChildModel extends ContextTabChildModel {

    // This is the context name to display in Context column.
    private String contextName;

    public ContextTreeTabChildModel() {
    }

    public ContextTreeTabChildModel(String contextName) {
        this.contextName = contextName;
    }

    public String getContextName() {
        return this.contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getPromptValue() {
        return contextParameter.getPrompt();
    }

    public String getCommentValue() {
        return contextParameter.getComment();
    }

}
