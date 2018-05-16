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
package org.talend.repository.items.importexport.handlers.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.talend.commons.CommonsPlugin;
import org.talend.core.model.repository.DynaEnum;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.ProductUtils;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * 
 * most of importers should extends this class, just recommend.
 */
@SuppressWarnings("rawtypes")
public class ImportRepTypeHandler extends ImportBasicHandler {

    /**
     * key is perspectiveId, value is type of path.
     */
    protected static Map<String, List<IPath>> product4PathMap = new HashMap<String, List<IPath>>();

    protected static final String PARAM_TYPE = "type"; //$NON-NLS-1$
    static {
        // temp work for TUP-1878, if want to enable to import all item in one dialog, should remove this.
        for (ERepositoryObjectType type : DynaEnum.values(ERepositoryObjectType.class)) {
            if (!type.isResouce()) {
                continue;
            }
            String[] products = type.getProducts();
            if (products != null) {
                for (String product : products) {
                    String perspectiveId = ProductUtils.getPerspectiveId(product);
                    List<IPath> list = product4PathMap.get(perspectiveId);
                    if (list == null) {
                        list = new ArrayList<IPath>();
                        product4PathMap.put(perspectiveId, list);
                    }
                    String folder = type.getFolder();
                    if (StringUtils.isNotEmpty(folder)) {
                        list.add(new Path(folder));
                    }
                }
            }
        }
        //

    }

    /**
     * set by extension point, will be item type of import items.
     * 
     * for example, for job designer, will be "ERepositoryObjectType.PROCCESS".
     */
    protected final Set<ERepositoryObjectType> checkedItemTypes = new HashSet<ERepositoryObjectType>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.AbstractImportExecutableHandler#setInitializationData
     * (org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
     */
    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        super.setInitializationData(config, propertyName, data);
        if (data != null && data instanceof Map) {
            Map parametersMap = (Map) data;
            Object object = parametersMap.get(PARAM_TYPE);
            if (object != null) {
                String[] typeStrs = object.toString().split(IImportConstants.SEP_COMMA);
                for (String typeStr : typeStrs) {
                    if (StringUtils.isNotEmpty(typeStr)) {
                        typeStr = typeStr.trim();
                        if (StringUtils.isNotEmpty(typeStr)) {
                            ERepositoryObjectType type = ERepositoryObjectType.getType(typeStr);
                            if (type != null) {
                                checkedItemTypes.add(type);
                                // if enable the base path, will use the path of type.
                                if (StringUtils.isNotEmpty(type.getFolder())) {
                                    checkedBasePathes.add(type.getFolder());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * temp resolve for TUP-1878.
     */
    @Override
    protected boolean validRelativePath(IPath relativePath) {
        boolean valid = super.validRelativePath(relativePath);
        if (valid && !CommonsPlugin.isHeadless() && isEnableProductChecking()) {
            String currentPerspectiveId = CoreRuntimePlugin.getInstance().getActivedPerspectiveId();
            if (StringUtils.isNotEmpty(currentPerspectiveId)) {
                List<IPath> pathes = product4PathMap.get(currentPerspectiveId);
                if (pathes != null) {
                    for (IPath typePath : pathes) {
                        if (typePath.isPrefixOf(relativePath)) {
                            return true; // the path is under current product.
                        }
                    }
                }
                return false; // in studio, if don't match, will be false.
            }
        }
        return valid;
    }

    @Override
    public boolean valid(ImportItem importItem) {
        boolean valid = super.valid(importItem);
        if (valid) {
            ERepositoryObjectType itemType = importItem.getType();
            if (itemType != null && checkedItemTypes.contains(itemType)) {
                // if in studio, check the product.
                if (!CommonsPlugin.isHeadless() && isEnableProductChecking()) {
                    String currentPerspectiveId = CoreRuntimePlugin.getInstance().getActivedPerspectiveId();
                    String[] products = itemType.getProducts();
                    if (products != null) {
                        for (String product : products) {
                            String perspectiveId = ProductUtils.getPerspectiveId(product);
                            if (currentPerspectiveId != null && currentPerspectiveId.equals(perspectiveId)) {
                                return true; // match the product and perspective.
                            }
                        }
                        return false; // if enable product check, have set the product.
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ImportItem generateImportItem(IProgressMonitor monitor, ResourcesManager resManager, IPath resourcePath,
            boolean overwrite, List<ImportItem> existedImportItems) throws Exception {
        // just do like createImportItem if no special cases
        return createImportItem(monitor, resManager, resourcePath, overwrite, existedImportItems);
    }

}
