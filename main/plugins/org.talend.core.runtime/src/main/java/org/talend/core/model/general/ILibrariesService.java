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
package org.talend.core.model.general;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.BusinessException;
import org.talend.core.IService;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.codegen.PigTemplate;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public interface ILibrariesService extends IService {

    public static final String SOURCE_PERL_ROUTINES_FOLDER = "routines"; //$NON-NLS-1$

    public static final String SOURCE_SQLPATTERN_FOLDER = "sqltemplates"; //$NON-NLS-1$

    public static final String SQLPATTERN_FILE_SUFFIX = ".sqltemplate"; //$NON-NLS-1$

    public static final String TEMPLATE_SUFFIX = ".template"; //$NON-NLS-1$

    public List<URL> getSystemRoutines();

    public List<URL> getSystemSQLPatterns();

    public List<URL> getTalendRoutinesFolder() throws IOException;

    public List<URL> getTalendBeansFolder() throws IOException;

    public List<URL> getTalendRoutines();

    public String getPerlLibrariesPath();

    public void deployLibrary(URL source, boolean reset) throws IOException;

    public void deployLibrary(URL source) throws IOException;

    public void deployLibrary(URL source, String mavenUri) throws IOException;

    public void deployLibrary(URL source, String mavenUri, boolean refresh) throws IOException;

    public void deployLibrary(URL source, String mavenUri, boolean refresh, boolean updateNexusJar) throws IOException;

    public void deployLibrarys(URL[] source) throws IOException;

    public void undeployLibrary(String jarName) throws IOException;

    public URL getRoutineTemplate();

    public URL getPigudfTemplate(PigTemplate tmplate);

    public URL getBeanTemplate();

    public URL getSqlPatternTemplate();

    /**
     * 
     * DOC wchen Comment method "getLibraryStatus".
     * 
     * @deprecated better call the function getLibraryStatus(String libName, String mvnURI)
     * @param libName
     * @return
     * @throws BusinessException
     */
    @Deprecated
    public ELibraryInstallStatus getLibraryStatus(String libName) throws BusinessException;

    public ELibraryInstallStatus getLibraryStatus(String libName, String mvnURI);

    public List<Problem> getProblems(INode node, IElement element);

    public void syncLibraries(IProgressMonitor... monitorWrap);

    @Deprecated
    public void syncLibrariesFromLibs(IProgressMonitor... monitorWrap);

    public void checkLibraries();

    public void addChangeLibrariesListener(IChangedLibrariesListener listener);

    public void removeChangeLibrariesListener(IChangedLibrariesListener listener);

    public void resetModulesNeeded();

    public void updateModulesNeededForCurrentJob(IProcess process);

    public boolean isLibSynchronized();

    void cleanLibs();

    Set<ModuleNeeded> getCodesModuleNeededs(ERepositoryObjectType type);

    List<ModuleNeeded> getModuleNeeded(String id, boolean isGroup);

    public void deployProjectLibrary(File source) throws IOException;

    /**
     * Listener used to fire that libraries status has been changed (new lib or new check install).
     * 
     * $Id$
     * 
     */
    public interface IChangedLibrariesListener {

        public void afterChangingLibraries();
    }

}
