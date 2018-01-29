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
package org.talend.repository.navigator;

/**
 * This interface is used to provide the top line string description in the Talend repository view this shall be adapted
 * to the repository navigator input
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public interface INavigatorDescriptor {

    /**
     * Return the descritor string for the view. never null
     * 
     * @return
     */
    public String getDescriptor();
}
