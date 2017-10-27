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
package org.talend.commons.utils.workbench.extensions;

/**
 * Defines an extension point. To be use in ExtensionImplementationProviders<br/>
 * 
 * $Id$
 * 
 */
public interface IExtensionPointLimiter {

    /**
     * Getter for extension point id.
     * 
     * @return the extPointId
     */
    public String getExtPointId();

    /**
     * Name of a Configuration element Can be null.
     * 
     * @return
     */
    public String getConfigurationElementName();

    /**
     * Getter for extension point maximum authorized implementation. Specify -1 for no max occurence
     * 
     * @return the maxOcc
     */
    public int getMaxOcc();

    public void setMaxOcc(int maxOcc);

    /**
     * Getter for extension point minimum authorized implementation. Specify -1 for no min occurence
     * 
     * @return the minOcc
     */
    public int getMinOcc();
}
