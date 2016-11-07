package org.talend.librariesmanager.model.service;

import java.util.Set;
import java.util.Vector;

import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.routines.IRoutinesService;
import org.talend.librariesmanager.model.ModulesNeededProvider;

import routines.TalendString;

/**
 * 
 * qli class global comment. Detailed comment
 */
public class RoutinesProviderService implements IRoutinesService {

    public Vector getAccents() {
        return new Vector(TalendString.getMap());
    }

    public Set<ModuleNeeded> getRunningModules() {
        return ModulesNeededProvider.getRunningModules();
    }
}
