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
package org.talend.registration;

import org.talend.commons.ui.runtime.image.IImage;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public enum ERepositoryImages implements IImage {

    REGISTER_ICO("/icons/register_icon.png"), //$NON-NLS-1$ 
    //    REGISTER_ICO("/icons/login_v.png"), //$NON-NLS-1$
    LICENSE_WIZ("/icons/license_wiz.png"), //$NON-NLS-1$
    DOT_ICON("/icons/dot.jpg"), //$NON-NLS-N$
    TALENDFORGE_ICON("/icons/talendforge.jpg"), //$NON-NLS-N$
    REGISTER_WIZ("/icons/register_wiz.png"); //$NON-NLS-1$

    private String path;

    ERepositoryImages(String path) {
        this.path = path;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for clazz.
     * 
     * @return the clazz
     */
    public Class getLocation() {
        return RegistrationPlugin.class;
    }

}
