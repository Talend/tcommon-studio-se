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
package org.talend.updates.runtime.nexus.component;

/**
 * DOC ggu class global comment. Detailled comment
 */
public enum ComponentIndexNames {
    /*
     * required
     */
    name(true),
    bundle_id(true),
    version(true),
    mvn_uri(true),

    image_mvn_uri,
    product,
    description,
    license_uri,
    license,
    types,
    categories,
    degradable,
    compatibleStudioVersion,
    //
    ;

    private final boolean required;

    private ComponentIndexNames(boolean required) {
        this.required = required;
    }

    private ComponentIndexNames() {
        this(false);
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        return name();
    }

}
