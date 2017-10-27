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
package org.talend.core.repository.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by ggu on Sep 15, 2014 Detailled comment
 *
 */
public abstract class AbstractShowJobStartup implements IStartup {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    @Override
    public void earlyStartup() {
        // only work for studio.
        if (CommonsPlugin.isHeadless()) {
            return;
        }
        String[] showJobNames = getShowJobNames();
        if (showJobNames != null && showJobNames.length > 0) { // existed
            ERepositoryObjectType[] showJobTypes = getShowJobTypes();
            if (showJobTypes != null) {
                IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                for (ERepositoryObjectType type : showJobTypes) {
                    if (!enableForType(type)) {
                        continue;
                    }
                    try {
                        List<IRepositoryViewObject> all = proxyRepositoryFactory.getAll(type);
                        for (IRepositoryViewObject viewObject : all) {
                            if (validJob(viewObject, showJobNames)) {
                                processJob(viewObject);
                            }
                        }
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }

    protected String[] getShowJobNames() {
        if (ArrayUtils.contains(Platform.getApplicationArgs(), EclipseCommandLine.TALEND_SHOW_JOB_COMMAND)) {
            String jobsStr = getAppArgValue(EclipseCommandLine.TALEND_SHOW_JOB_COMMAND, ""); //$NON-NLS-1$
            if (jobsStr != null && jobsStr.length() > 0) { // existed
                String[] jobNames = getArgSplitValues(jobsStr);
                return jobNames;
            }
        }
        return new String[0];
    }

    protected ERepositoryObjectType[] getShowJobTypes() {
        List<ERepositoryObjectType> typesList = new ArrayList<ERepositoryObjectType>();
        // if not set the --showJobType <type>, the type will be PROCESS by default.
        String jobTypesStr = getAppArgValue(EclipseCommandLine.TALEND_SHOW_JOB_TYPE_COMMAND,
                ERepositoryObjectType.PROCESS.getType());
        if (jobTypesStr != null && jobTypesStr.length() > 0) { // existed
            String[] jobTypes = getArgSplitValues(jobTypesStr);
            for (String type : jobTypes) {
                ERepositoryObjectType repoType = ERepositoryObjectType.valueOf(type);
                if (repoType != null) {
                    typesList.add(repoType);
                } else {
                    CommonExceptionHandler.warn("It's not valid type: " + type); //$NON-NLS-1$
                }
            }
        }
        return typesList.toArray(new ERepositoryObjectType[0]);
    }

    private String getAppArgValue(String arg, String defaultValue) {
        String value = defaultValue;
        int index = ArrayUtils.indexOf(Platform.getApplicationArgs(), arg);
        if (index > -1) { // found
            if (index + 1 < Platform.getApplicationArgs().length) {
                value = Platform.getApplicationArgs()[index + 1];
            }
        }
        return value;
    }

    private String[] getArgSplitValues(String value) {
        return value.split("[,;]"); //$NON-NLS-1$
    }

    /**
     * Enable to filter the type for each implmentation
     */
    protected boolean enableForType(ERepositoryObjectType type) {
        return true; // by default, enable for all
    }

    /**
     * Enable to check the case sensitive for name.
     * 
     */
    protected boolean validJob(IRepositoryViewObject viewObject, String[] showJobNames) {
        if (viewObject != null) {
            String label = viewObject.getLabel();
            for (String name : showJobNames) {
                if (label.equals(name) || !isCaseSensitiveForName() && label.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isCaseSensitiveForName() {
        return true;
    }

    /**
     * 
     * will process the object as you want.
     */
    protected abstract void processJob(IRepositoryViewObject viewObject);
}
