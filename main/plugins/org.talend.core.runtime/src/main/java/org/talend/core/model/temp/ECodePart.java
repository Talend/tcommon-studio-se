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
package org.talend.core.model.temp;

/**
 * Code parts are used by the Code Generator to generate differents components codes in a different order depending the
 * order of the components in the process.
 * 
 * $Id: ECodePart.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public enum ECodePart {
    MAIN("main"), //$NON-NLS-1$
    PROCESS_DATA_BEGIN("process_data_begin"), //$NON-NLS-1$
    PROCESS_DATA_END("process_data_end"), //$NON-NLS-1$
    BEGIN("begin"), //$NON-NLS-1$
    END_HEAD("end_head"), //$NON-NLS-1$
    END_BODY("end_body"), //$NON-NLS-1$
    END_TAIL("end_tail"), //$NON-NLS-1$
    END("end"), //$NON-NLS-1$
    FINALLY("finally"), //$NON-NLS-1$
    MRCODE("mrcode"), //$NON-NLS-1$
    MRCONFIG("mrconfig"), //$NON-NLS-1$
    MRJOBFOOTER("mrjobfooter"), //$NON-NLS-1$
    STORMCODE("stormcode"), //$NON-NLS-1$
    STORMCONFIG("stormconfig"), //$NON-NLS-1$
    STORMMONITOR("stormmonitor"), //$NON-NLS-1$
    STORMJOBFOOTER("stormjobfooter"), //$NON-NLS-1$
    SPARKCODE("sparkcode"), //$NON-NLS-1$
    SPARKCONFIG("sparkconfig"), //$NON-NLS-1$
    SPARKJOBFOOTER("sparkjobfooter"), //$NON-NLS-1$
    SPARKPOSTPROCESSING("sparkpostprocessing"), //$NON-NLS-1$
    SPARKSTREAMINGCODE("sparkstreamingcode"), //$NON-NLS-1$
    SPARKSTREAMINGCONFIG("sparkstreamingconfig"), //$NON-NLS-1$
    SPARKSTREAMINGJOBFOOTER("sparkstreamingjobfooter"), //$NON-NLS-1$
    SPARKSTREAMINGPOSTPROCESSING("sparkstreamingpostprocessing"); //$NON-NLS-1$

    private ECodePart(String name) {
        this.name = name;
    }

    private String name;

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    public static ECodePart getCodePartByName(String name) {
        for (ECodePart part : values()) {
            if (part.getName().equals(name)) {
                return part;
            }
        }
        return null;
    }
}
