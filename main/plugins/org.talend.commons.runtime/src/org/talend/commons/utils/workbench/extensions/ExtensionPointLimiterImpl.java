// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.workbench.extensions;

/**
 * Default implementation of IExtensionPoint.
 */
public class ExtensionPointLimiterImpl implements IExtensionPointLimiter {

    String extPointId;

    String elementName;

    int minOcc = -1;

    int maxOcc = -1;

    /**
     * @param extPointId
     * @param elementName can be null
     * @param namespaceIdentifier can be null
     * @param minOcc
     * @param maxOcc
     */
    public ExtensionPointLimiterImpl(String extPointId, String elementName, int minOcc, int maxOcc) {
        super();
        this.extPointId = extPointId;
        this.elementName = elementName;
        this.minOcc = minOcc;
        this.maxOcc = maxOcc;
    }

    public ExtensionPointLimiterImpl(String extPointId, int minOcc, int maxOcc) {
        super();
        this.extPointId = extPointId;
        this.minOcc = minOcc;
        this.maxOcc = maxOcc;
    }

    public ExtensionPointLimiterImpl(String extPointId) {
        super();
        this.extPointId = extPointId;
    }

    public ExtensionPointLimiterImpl(String extPointId, String elementName) {
        super();
        this.extPointId = extPointId;
        this.elementName = elementName;
    }

    public void setExtPointId(String extPointId) {
        this.extPointId = extPointId;
    }

    public void setMaxOcc(int maxOcc) {
        this.maxOcc = maxOcc;
    }

    public void setMinOcc(int minOcc) {
        this.minOcc = minOcc;
    }

    public String getExtPointId() {
        return extPointId;
    }

    public int getMaxOcc() {
        return maxOcc;
    }

    public int getMinOcc() {
        return minOcc;
    }

    public String getConfigurationElementName() {
        return this.elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

}
