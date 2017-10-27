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
package org.talend.core.model.process;

import java.net.URL;

/**
 * This interface is used for storing component documentation information.
 * 
 */
public interface IComponentDocumentation {

    /**
     * Gets the url of html file of current component.
     * 
     * @return
     */
    public URL getHTMLFile();

    /**
     * Sets the path.
     * 
     * @param tempFolderPath
     * @return
     */
    public void setTempFolderPath(String tempFolderPath);

    /**
     * Sets the component name
     * 
     * @param componentName
     * @return
     */
    public void setComponentName(String componentName);

}
