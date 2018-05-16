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
package org.talend.core.services;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.core.IService;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.INode;
import org.talend.core.updatesite.IUpdateSiteBean;

/**
 * wchen class global comment. Detailled comment
 */
public interface ICoreTisService extends IService {

    public void drawWelcomeLogo(String version);

    public boolean isSupportDynamicType(INode node);

    public boolean validProject(Project project, boolean flag) throws PersistenceException;

    public List<IUpdateSiteBean> getPatchesInstalled() throws BackingStoreException;

    public boolean needRestartAfterUpdate();

    public void setNeedResartAfterUpdate(boolean needRestart);

    /**
     * DOC ycbai Comment method "exportAsCWM".
     * 
     * @param itemUri
     * @param destDir
     */
    public void exportAsCWM(URI itemUri, String destDir);
    
    public boolean needUpdate(String userName, String password, String adminUrl)  throws SystemException;

    public void downLoadAndInstallUpdates(String userName, String password, String adminUrl) throws Exception;
    
    public boolean isLicenseExpired();
    
    public boolean isTheSameType(String userName, String password, String adminUrl);

    public void clearCustomLicensePathArg();
}
