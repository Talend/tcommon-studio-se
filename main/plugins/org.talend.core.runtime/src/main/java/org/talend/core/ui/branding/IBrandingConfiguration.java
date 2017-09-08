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
package org.talend.core.ui.branding;

import java.util.List;
import java.util.Map;

import org.eclipse.ui.IPageLayout;
import org.talend.core.utils.ProductUtils;
import org.talend.repository.model.IRepositoryNode;

/**
 * BrandingConfigure for each application's customize setting E.g /perspective/menus/repository
 * view/palletteview/coolbars etc.
 */
public interface IBrandingConfiguration extends IActionBarHelper {

    public static final String PERSPECTIVE_DI_ID = ProductUtils.PERSPECTIVE_DI_ID;

    public static final String PERSPECTIVE_DQ_ID = ProductUtils.PERSPECTIVE_DQ_ID;

    public static final String PERSPECTIVE_MDM_ID = ProductUtils.PERSPECTIVE_MDM_ID;

    /**
     * 
     * init perspective.
     * 
     * @param layout
     */
    public void initPerspective(IPageLayout layout);

    /**
     * 
     * get the repositorynode you want to hide in repositoryview.
     * 
     * @return
     * @deprecated try to remove this hidden function by CNF in extension point.
     * @see point="org.eclipse.ui.navigator.navigatorContent"
     */
    @Deprecated
    public List<IRepositoryNode> getHiddenRepositoryCategory(IRepositoryNode parent, String type);

    /**
     * returns null if all components are available.
     */
    public String[] getAvailableComponents();

    public void setHelper(IActionBarHelper helper);

    public IActionBarHelper getHelper();

    public String[] getAvailableLanguages();

    public Map<String, Object> getJobEditorSettings();

    public boolean isUseMailLoginCheck();

    public boolean isUseProductRegistration();

    public boolean isAllowDebugMode();

    public boolean isAllowChengeVersion();

    public boolean isUseDemoProjects();

    public boolean isOnlyRemoteConnection();

    public String getAdditionalTitle();

    public void setAdditionalTitle(String title);

    public String getInitialWindowPerspectiveId();

    public void setUseMailLoginCheck(boolean useMainLoginCheck);

    public void setUseProductRegistration(boolean useProductRegistration);

}
