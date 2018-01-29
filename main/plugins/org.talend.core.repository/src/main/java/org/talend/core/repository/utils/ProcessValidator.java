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
package org.talend.core.repository.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * Created by Marvin Wang on Mar 27, 2013.
 */
public class ProcessValidator {

    private static ProcessValidator instance;

    private ProcessValidator() {
    }

    public static synchronized ProcessValidator getInstance() {
        if (instance == null) {
            instance = new ProcessValidator();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public boolean isValidated(Property property, String name) throws PersistenceException {
        boolean isValidated = false;
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        List<IRepositoryViewObject> all = extractAllRepositoryViewObjects();
        isValidated = factory.isNameAvailable(property.getItem(), name, all);
        return isValidated;
    }

    protected List<IRepositoryViewObject> extractAllRepositoryViewObjects() throws PersistenceException {
        List<IRepositoryViewObject> all = new ArrayList<IRepositoryViewObject>();
        // All repository view objects for process.
        if (ERepositoryObjectType.PROCESS != null) {
            List<IRepositoryViewObject> allProcesses = extractRepViewObjByType(ERepositoryObjectType.PROCESS);
            all.addAll(allProcesses);
        }

        // All rep view objs for m/r process
        if (ERepositoryObjectType.PROCESS_MR != null) {
            List<IRepositoryViewObject> allMRProcesses = extractRepViewObjByType(ERepositoryObjectType.PROCESS_MR);
            all.addAll(allMRProcesses);
        }

        // All rep view objs for routines.
        if (ERepositoryObjectType.ROUTINES != null) {
            List<IRepositoryViewObject> allRoutines = extractRepViewObjByType(ERepositoryObjectType.ROUTINES);
            all.addAll(allRoutines);
        }
        return all;
    }

    protected List<IRepositoryViewObject> extractRepViewObjByType(ERepositoryObjectType type) throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);

            return service.getProxyRepositoryFactory().getAll(type, true, false);
        }
        return list;
    }
}
