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
package org.talend.core.model.process;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.talend.commons.CommonsPlugin;
import org.talend.core.model.properties.Item;
import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * DOC xhuang class global comment. Detailled comment <br/>
 * 
 */
public class TalendProblem extends Problem {

    private IMarker marker;

    private String javaUnitName;

    private String unitName;

    private String version;

    private Integer lineNumber;

    private Integer charStart;

    private Integer charEnd;

    public TalendProblem(ProblemStatus status, String javaUnitName, IMarker marker, String markerErrorMessage,
            Integer lineNumber, String uniName, Integer charStart, Integer charEnd, ProblemType type, String version) {
        super();
        setDescription(markerErrorMessage);
        setStatus(status);

        if (!CommonsPlugin.isHeadless()) {
            List<IProcess2> openedProcessList = CoreRuntimePlugin.getInstance().getDesignerCoreService()
                    .getOpenedProcess(getEditors());
            for (IProcess2 process : openedProcessList) {
                if (javaUnitName != null && javaUnitName.equals(process.getName())) {
                    BasicJobInfo jobInfo = new BasicJobInfo(process.getId(), null, process.getVersion());
                    jobInfo.setJobName(process.getName());
                    setJobInfo(jobInfo);
                }
            }
        }
        this.javaUnitName = javaUnitName;
        this.marker = marker;
        this.lineNumber = lineNumber;
        this.unitName = uniName;
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.version = version;
        setType(type);
        setNodeName(uniName);
    }

    public TalendProblem(ProblemStatus status, Item item, IMarker marker, String markerErrorMessage, Integer lineNumber,
            String uniName, Integer charStart, Integer charEnd, ProblemType type) {
        super();
        setDescription(markerErrorMessage);
        setStatus(status);

        if (item.getProperty().getId() != null) {
            BasicJobInfo jobInfo = new BasicJobInfo(item.getProperty().getId(), null, item.getProperty().getVersion());
            jobInfo.setJobName(item.getProperty().getLabel());
            setJobInfo(jobInfo);
        }

        this.javaUnitName = item.getProperty().getLabel();
        this.marker = marker;
        this.lineNumber = lineNumber;
        this.unitName = uniName;
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.version = item.getProperty().getVersion();
        setType(type);
        setNodeName(uniName);
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    private String getName() {
        return javaUnitName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public Integer getCharStart() {
        return this.charStart;
    }

    public Integer getCharEnd() {
        return this.charEnd;
    }

    public String getJavaUnitName() {
        return this.javaUnitName;
    }

    @Override
    public String getProblemResource() {
        return this.type.getTypeName() + ":" + this.getName() + " (line:" + this.getLineNumber() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    //

    public IMarker getMarker() {
        return this.marker;
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
        result = prime * result + ((this.javaUnitName == null) ? 0 : this.javaUnitName.hashCode());
        result = prime * result + ((this.lineNumber == null) ? 0 : this.lineNumber.hashCode());
        result = prime * result + ((this.marker == null) ? 0 : this.marker.hashCode());
        result = prime * result + ((this.charStart == null) ? 0 : this.charStart.hashCode());
        result = prime * result + ((this.charEnd == null) ? 0 : this.charEnd.hashCode());
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
        final TalendProblem other = (TalendProblem) obj;
        if (this.javaUnitName == null) {
            if (other.javaUnitName != null) {
                return false;
            }
        } else if (!this.javaUnitName.equals(other.javaUnitName)) {
            return false;
        }
        if (this.lineNumber == null) {
            if (other.lineNumber != null) {
                return false;
            }
        } else if (!this.lineNumber.equals(other.lineNumber)) {
            return false;
        }
        if (this.marker == null) {
            if (other.marker != null) {
                return false;
            }
        } else if (!this.marker.equals(other.marker)) {
            return false;
        }
        if (this.charStart == null) {
            if (other.charStart != null) {
                return false;
            }
        } else if (!this.charStart.equals(other.charStart)) {
            return false;
        }
        if (this.charEnd == null) {
            if (other.charEnd != null) {
                return false;
            }
        } else if (!this.charEnd.equals(other.charEnd)) {
            return false;
        }
        if (this.getDescription() == null) {
            if (other.getDescription() != null) {
                return false;
            }
        } else if (!this.getDescription().equals(other.getDescription())) {
            return false;
        }
        return true;
    }

}
