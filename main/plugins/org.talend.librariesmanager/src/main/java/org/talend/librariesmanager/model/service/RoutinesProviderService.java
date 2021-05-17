package org.talend.librariesmanager.model.service;

import java.util.Vector;

import org.talend.core.model.routines.IRoutinesService;

import routines.TalendString;

/**
 * 
 * qli class global comment. Detailled comment
 */
public class RoutinesProviderService implements IRoutinesService {

    public Vector getAccents() {
        return new Vector(TalendString.getMap());
    }

}
