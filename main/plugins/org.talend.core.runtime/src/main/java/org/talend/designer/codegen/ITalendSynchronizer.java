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
package org.talend.designer.codegen;

import org.eclipse.core.resources.IFile;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PigudfItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public interface ITalendSynchronizer {

    String TEMPLATE = "__TEMPLATE__"; //$NON-NLS-1$

    void syncAllRoutines() throws SystemException;

    void syncAllRoutinesForLogOn() throws SystemException;

    void syncAllPigudf() throws SystemException;

    void syncAllPigudfForLogOn() throws SystemException;

    void syncAllBeans() throws SystemException;

    void syncAllBeansForLogOn() throws SystemException;

    void syncRoutine(RoutineItem routineItem, boolean copyToTemp) throws SystemException;

    void syncRoutine(RoutineItem routineItem, boolean copyToTemp, boolean forceToUpdate) throws SystemException;

    IFile getFile(Item item) throws SystemException;

    IFile getRoutinesFile(Item routineItem) throws SystemException;

    void forceSyncRoutine(RoutineItem routineItem);

    abstract void renameRoutineClass(RoutineItem routineItem);

    abstract void renamePigudfClass(PigudfItem routineItem, String oldLabel);

    void deleteRoutinefile(IRepositoryViewObject objToDelete);

}
