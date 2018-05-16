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
package org.talend.core.model.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Class that will be used in the ProblemsView. <br/>
 * 
 * $Id: Problem.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class Problem {

    public final static String EMPTY_STRING = "";//$NON-NLS-1$

    public static final Problem[] EMPTY_PROBLEM_ARRAY = new Problem[0];

    protected static final Collection<Problem> EMPTY_PROBLEM_COLLECTION = Arrays.asList(new Problem[0]);

    /**
     * smallet Problem class global comment. Detailled comment <br/>
     * 
     * $Id: Problem.java 48513 2010-09-18 14:56:23Z nrousseau $
     */
    public enum ProblemStatus {
        ERROR,
        WARNING,
        INFO
    }

    /**
     * bqian Problem class global comment. Detailled comment <br/>
     */
    public enum ProblemType {
        JOB("Job"), //$NON-NLS-1$
        ROUTINE("Routine"), //$NON-NLS-1$
        NONE(""); //$NON-NLS-1$

        private String typeName;

        ProblemType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return this.typeName;
        }
    }

    /**
     * Added to enhance the refresh speed of the problems view. <br/>
     * 
     * $Id: Problem.java 48513 2010-09-18 14:56:23Z nrousseau $
     * 
     */

    private String description;

    private ProblemStatus status;

    protected ProblemType type = ProblemType.NONE;

    private String key;

    private BasicJobInfo jobInfo;

    private String nodeName;

    private String componentName;

    /**
     * DOC smallet Problem constructor comment.
     */
    public Problem() {
        super();
    }

    /**
     * DOC smallet Problem constructor comment.
     * 
     * @param element
     * @param description
     * @param status
     */
    public Problem(IElement element, String description, ProblemStatus status) {
        super();
        this.description = description;
        this.status = status;
        setElement(element);
    }

    /**
     * Getter for JobInfo.
     * 
     * @return the JobInfo
     */
    public BasicJobInfo getJobInfo() {
        return this.jobInfo;
    }

    public void setJobInfo(BasicJobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public static IEditorReference[] getEditors() {
        final List<IEditorReference> list = new ArrayList<IEditorReference>();

        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                // workbench should be created,bug 22659
                if (PlatformUI.isWorkbenchRunning()) {
                    IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    if (activeWorkbenchWindow == null) {
                        return;
                    }
                    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                    if (activePage == null) {
                        return;
                    }
                    IEditorReference[] reference = activePage.getEditorReferences();
                    list.addAll(Arrays.asList(reference));
                }
            }
        });
        return list.toArray(new IEditorReference[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.jobInfo == null) ? 0 : this.jobInfo.hashCode());
        result = prime * result + ((this.nodeName == null) ? 0 : this.nodeName.hashCode());
        result = prime * result + ((this.componentName == null) ? 0 : this.componentName.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Problem other = (Problem) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.jobInfo == null) {
            if (other.jobInfo != null) {
                return false;
            }
        } else if (!this.jobInfo.equals(other.jobInfo)) {
            return false;
        }
        if (this.nodeName == null) {
            if (other.nodeName != null) {
                return false;
            }
        } else if (!this.nodeName.equals(other.nodeName)) {
            return false;
        }
        if (this.componentName == null) {
            if (other.componentName != null) {
                return false;
            }
        } else if (!this.componentName.equals(other.componentName)) {
            return false;
        }
        if (this.status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!this.status.equals(other.status)) {
            return false;
        }
        return true;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setElement(IElement element) {
        if (element instanceof INode) {
            jobInfo = new BasicJobInfo(((INode) element).getProcess().getId(), null, ((INode) element).getProcess().getVersion());
            jobInfo.setJobName(((INode) element).getProcess().getName());
            type = ProblemType.JOB;
            /* nodeName should use uniqueName,see bug 20560 */
            nodeName = ((INode) element).getUniqueName();
            componentName = ((INode) element).getComponent().getName();
        } else if (element instanceof IProcess) {
            jobInfo = new BasicJobInfo(((IProcess) element).getId(), null, ((IProcess) element).getVersion());
            jobInfo.setJobName(((IProcess) element).getName());
            type = ProblemType.JOB;
        }

    }

    /**
     * Getter for status.
     * 
     * @return the status
     */
    public ProblemStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * 
     * @param status the status to set
     */
    public void setStatus(ProblemStatus status) {
        this.status = status;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Sets the key.
     * 
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    public Problem[] getChildren() {
        return EMPTY_PROBLEM_ARRAY;
    }

    public boolean isConcrete() {
        return true;
    }

    /**
     * bqian Comment method "getName".
     * 
     * @return
     */
    public String getProblemResource() {
        if (getType().equals(ProblemType.JOB)) {
            return "Job:" + jobInfo.getJobName() + "  (component:" + nodeName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } else {
            // TODO need to prcess the display of routine here.
        }
        return Problem.EMPTY_STRING;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public ProblemType getType() {
        return this.type;
    }

    /**
     * Sets the type.
     * 
     * @param type the type to set
     */
    public void setType(ProblemType type) {
        this.type = type;
    }

    /**
     * Getter for nodeName.
     * 
     * @return the nodeName
     */
    public String getNodeName() {
        return this.nodeName;
    }

    protected void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Getter for componentName.
     * 
     * @return the componentName
     */
    public String getComponentName() {
        return this.componentName;
    }

}
