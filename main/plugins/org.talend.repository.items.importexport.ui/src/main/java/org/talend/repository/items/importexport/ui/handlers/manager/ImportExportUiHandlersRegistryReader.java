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
package org.talend.repository.items.importexport.ui.handlers.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.core.utils.RegistryReader;
import org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportExportUiHandlersRegistryReader extends RegistryReader {

    private static Logger log = Logger.getLogger(ImportExportUiHandlersRegistryReader.class);

    /**
     * key is id of helper
     */
    private final Map<String, ActionHelperRegistry> actionsHelperMap = new HashMap<String, ActionHelperRegistry>();

    private IImportExportItemsActionHelper[] actionsHelpers;

    public ImportExportUiHandlersRegistryReader() {
        super("org.talend.repository.items.importexport.ui", "importexportUiHandler"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void init() {
        readRegistry();

        // process the override
        Map<String, ActionHelperRegistry> processedActionsHelperMap = new HashMap<String, ActionHelperRegistry>(actionsHelperMap);
        List<String> overrideHelperIds = new ArrayList<String>();
        for (String key : actionsHelperMap.keySet()) {
            ActionHelperRegistry actionHelperRegistry = actionsHelperMap.get(key);
            if (actionHelperRegistry != null && actionHelperRegistry.getOverrideId() != null) {
                overrideHelperIds.add(actionHelperRegistry.getOverrideId());
            }
        }

        // remove the override helpers
        for (String key : overrideHelperIds) {
            processedActionsHelperMap.remove(key);
        }
        //
        List<IImportExportItemsActionHelper> leftHelpers = new ArrayList<IImportExportItemsActionHelper>();
        for (String key : processedActionsHelperMap.keySet()) {
            ActionHelperRegistry actionHelperRegistry = processedActionsHelperMap.get(key);
            IImportExportItemsActionHelper actionHelper = actionHelperRegistry.getActionHelper();
            if (actionHelper != null) {
                leftHelpers.add(actionHelper);
            }
        }

        actionsHelpers = leftHelpers.toArray(new IImportExportItemsActionHelper[0]);
    }

    IImportExportItemsActionHelper[] getActionsHelpers() {
        return this.actionsHelpers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("actionHelper".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    try {
                        String id = element.getAttribute("id"); //$NON-NLS-1$
                        String description = element.getAttribute("description"); //$NON-NLS-1$
                        String overrideId = element.getAttribute("override"); //$NON-NLS-1$

                        IImportExportItemsActionHelper importItemsHandler = (IImportExportItemsActionHelper) element
                                .createExecutableExtension("class"); //$NON-NLS-1$
                        if (importItemsHandler == null) {
                            log.error("Can't create handlder for " + id); //$NON-NLS-1$
                            return;
                        }
                        if (actionsHelperMap.containsKey(id)) {
                            log.error("Can't set the id same as existed id " + id); //$NON-NLS-1$
                            return;
                        }
                        ActionHelperRegistry registry = new ActionHelperRegistry(id, importItemsHandler);
                        registry.setDescription(description);
                        registry.setOverrideId(overrideId);
                        actionsHelperMap.put(id, registry);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }

                }

            });
            return true;

        }
        return false;
    }

}
