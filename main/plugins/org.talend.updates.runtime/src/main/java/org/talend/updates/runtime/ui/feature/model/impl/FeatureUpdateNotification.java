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
package org.talend.updates.runtime.ui.feature.model.impl;

import java.util.Collection;

import org.talend.updates.runtime.ui.feature.model.IFeatureUpdateNotification;
import org.talend.updates.runtime.ui.feature.model.Message;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureUpdateNotification extends AbstractFeatureItem implements IFeatureUpdateNotification {

    private String description;

    private Collection<Message> messages;

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Collection<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

}
