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
package org.talend.commons.runtime.model.emf.provider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public enum ResourceOption implements IOptionProvider {

    CREATATION,
    MIGRATION,
    ITEM_IMPORTATION,
    DEMO_IMPORTATION, ;

    private final OptionProvider provider;

    private ResourceOption() {
        this(Boolean.TRUE);
    }

    private ResourceOption(final Object value) {
        this.provider = new OptionProvider() {

            @Override
            public String getName() {
                return getName0();
            }

            @Override
            public Object getValue() {
                return value;
            }

        };
    }

    public OptionProvider getProvider() {
        return provider;
    }

    public String getName() {
        return getName0();
    }

    private String getName0() {
        return ("option_" + name()).toLowerCase();
    }

    @Override
    public Object getValue() {
        return getProvider();
    }

}
