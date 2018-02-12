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
package org.talend.repository;

import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;

/**
 * @param <T>
 */
public abstract class RepositoryWorkUnit<T> {

    private String name;

    private Project project;

    protected T result;

    private LoginException loginException;

    private PersistenceException persistenceException;

    private Object startingPoint;

    private boolean forceTransaction;

    private boolean avoidUnloadResourcesBeforeRun;

    private boolean unloadResourcesAfterRun;

    private boolean filesModifiedOutsideOfRWU;

    private boolean avoidUpdateLocks;

    private boolean refreshRepository = true; // added for TDI-27085

    /**
     * Usefull for some save only actions, where we're sure everything is up to date.
     */
    private boolean avoidSvnUpdate;

    public Object getStartingPoint() {
        return startingPoint;
    }

    public T getResult() {
        return result;
    }

    public RepositoryWorkUnit(String name) {
        this.name = name;
    }

    public RepositoryWorkUnit(String name, Object startingPoint) {
        this(name);
        this.startingPoint = startingPoint;
    }

    public RepositoryWorkUnit(Project project, String name) {
        this.project = project;
        this.name = name;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public void setLoginException(LoginException loginException) {
        this.loginException = loginException;
    }

    public void setPersistenceException(PersistenceException persistenceException) {
        this.persistenceException = persistenceException;
    }

    protected abstract void run() throws LoginException, PersistenceException;

    public void executeRun() {
        try {
            run();
        } catch (LoginException e) {
            setLoginException(e);
        } catch (PersistenceException e) {
            setPersistenceException(e);
        }
    }

    public void throwLoginExceptionIfAny() throws LoginException {
        if (loginException != null) {
            throw loginException;
        }
    }

    public void throwPersistenceExceptionIfAny() throws PersistenceException {
        if (persistenceException != null) {
            throw persistenceException;
        }
    }

    public boolean isForceTransaction() {
        return this.forceTransaction;
    }

    public void setForceTransaction(boolean forceTransaction) {
        this.forceTransaction = forceTransaction;
    }

    /**
     * Getter for avoidUnloadResources.
     * 
     * @return the avoidUnloadResources
     */
    public boolean isAvoidUnloadResources() {
        return this.avoidUnloadResourcesBeforeRun;
    }

    /**
     * Sets the avoidUnloadResources.
     * 
     * @param avoidUnloadResources the avoidUnloadResources to set
     */
    public void setAvoidUnloadResources(boolean avoidUnloadResources) {
        this.avoidUnloadResourcesBeforeRun = avoidUnloadResources;
    }

    public boolean isUnloadResourcesAfterRun() {
        return unloadResourcesAfterRun;
    }

    public void setUnloadResourcesAfterRun(boolean unloadResourcesAfterRun) {
        this.unloadResourcesAfterRun = unloadResourcesAfterRun;
    }

    /**
     * Getter for avoidSvnUpdate.
     * 
     * @return the avoidSvnUpdate
     */
    public boolean isAvoidSvnUpdate() {
        return this.avoidSvnUpdate;
    }

    /**
     * Sets the avoidSvnUpdate.
     * 
     * @param avoidSvnUpdate the avoidSvnUpdate to set
     */
    public void setAvoidSvnUpdate(boolean avoidSvnUpdate) {
        this.avoidSvnUpdate = avoidSvnUpdate;
    }

    /**
     * Getter for filesModifiedOutsideOfRWU.
     * 
     * @return the filesModifiedOutsideOfRWU
     */
    public boolean isFilesModifiedOutsideOfRWU() {
        return this.filesModifiedOutsideOfRWU;
    }

    /**
     * Sets the filesModifiedOutsideOfRWU.
     * 
     * @param filesModifiedOutsideOfRWU the filesModifiedOutsideOfRWU to set
     */
    public void setFilesModifiedOutsideOfRWU(boolean filesModifiedOutsideOfRWU) {
        this.filesModifiedOutsideOfRWU = filesModifiedOutsideOfRWU;
    }

    public boolean isRefreshRepository() {
        return this.refreshRepository;
    }

    public void setRefreshRepository(boolean refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

    
    /**
     * Getter for avoidUpdateLocks.
     * @return the avoidUpdateLocks
     */
    public boolean isAvoidUpdateLocks() {
        return this.avoidUpdateLocks;
    }

    
    /**
     * Sets the avoidUpdateLocks.
     * @param avoidUpdateLocks the avoidUpdateLocks to set
     */
    public void setAvoidUpdateLocks(boolean avoidUpdateLocks) {
        this.avoidUpdateLocks = avoidUpdateLocks;
    }
}
