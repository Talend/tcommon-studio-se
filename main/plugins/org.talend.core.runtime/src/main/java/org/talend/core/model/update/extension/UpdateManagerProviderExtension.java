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
package org.talend.core.model.update.extension;

/**
 * DOC ggu class global comment. Detailled comment
 */
class UpdateManagerProviderExtension {

    private final String id;

    private String name, description, overrideId;

    private EPriority priority;

    private IUpdateManagerProvider provider;

    public UpdateManagerProviderExtension(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EPriority getPriority() {
        return this.priority;
    }

    public void setPriority(EPriority priority) {
        this.priority = priority;
    }

    public IUpdateManagerProvider getProvider() {
        return this.provider;
    }

    public void setProvider(IUpdateManagerProvider provider) {
        this.provider = provider;
    }

    public String getOverrideId() {
        return this.overrideId;
    }

    public void setOverrideId(String overrideId) {
        this.overrideId = overrideId;
    }

}
