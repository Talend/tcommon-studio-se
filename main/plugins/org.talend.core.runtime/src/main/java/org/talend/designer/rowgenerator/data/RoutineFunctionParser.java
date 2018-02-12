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
package org.talend.designer.rowgenerator.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.internal.core.SourceType;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-3-28 下午02:37:17 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class RoutineFunctionParser extends AbstractTalendFunctionParser {

    private List<String> systems;

    /**
     * qzhang RoutineFunctionParser constructor comment.
     */
    public RoutineFunctionParser() {
        super();
    }

    @Override
    @SuppressWarnings("restriction")
    public void parse() {
        typeMethods.clear();
        systems = new ArrayList<String>();
        try {
            if (!GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
                return;
            }
            IRepositoryService rpositoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IRepositoryService.class);

            IProxyRepositoryFactory factory = rpositoryService.getProxyRepositoryFactory();
            RootContainer<String, IRepositoryViewObject> routineContainer = factory.getMetadata(ERepositoryObjectType.ROUTINES);
            final List<Container<String, IRepositoryViewObject>> subContainer = routineContainer.getSubContainer();
            for (Container<String, IRepositoryViewObject> container : subContainer) {
                if (RepositoryConstants.SYSTEM_DIRECTORY.equals(container.getLabel())) {
                    final List<IRepositoryViewObject> members = container.getMembers();
                    for (IRepositoryViewObject object : members) {
                        systems.add(object.getLabel());
                    }
                }
            }
            super.parse();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.rowgenerator.data.AbstractTalendFunctionParser#processSourceType(org.eclipse.jdt.internal
     * .core.SourceType)
     */
    @SuppressWarnings("restriction")
    @Override
    protected void processSourceType(IMember member, String className, String fullName, String funcName, boolean isSystem) {
        try {
            if (member instanceof SourceType) {
                IMethod[] methods = ((SourceType) member).getMethods();
                for (IMethod method : methods) {
                    super.processSourceType(method, className, fullName, method.getElementName(), systems.contains(className));
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.rowgenerator.data.AbstractTalendFunctionParser#getPackageFragment()
     */
    @Override
    protected String getPackageFragment() {
        return JavaUtils.JAVA_ROUTINES_DIRECTORY;
    }

    @Override
    protected ITalendProcessJavaProject getTalendCodeProject() {
        if(GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
            return service.getTalendCodeJavaProject(ERepositoryObjectType.ROUTINES);
        }
        return null;
    }
}
