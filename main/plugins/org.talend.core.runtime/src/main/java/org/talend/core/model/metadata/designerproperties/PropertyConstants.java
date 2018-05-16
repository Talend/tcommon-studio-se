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
package org.talend.core.model.metadata.designerproperties;

/**
 * cli class global comment. Detailled comment
 */
public final class PropertyConstants {

    private PropertyConstants() {
    }

    /**
     * work for cdc.
     * 
     * cli CDCTypeMode class global comment. Detailled comment
     */
    public enum CDCTypeMode {
        LOG_MODE("LOG"), //$NON-NLS-1$
        TRIGGER_MODE("TRIGGER"), //$NON-NLS-1$
        XSTREAM_MODE("XSTREAM"), //$NON-NLS-1$
        ;

        private String value;

        private CDCTypeMode(String value) {
            this.value = value;
        }

        public String getName() {
            return this.value;
        }

        public static CDCTypeMode indexOf(String value) {
            for (CDCTypeMode mode : CDCTypeMode.values()) {
                if (mode.getName().equals(value)) {
                    return mode;
                }
            }
            return null;
        }
    }

}
