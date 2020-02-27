package org.talend.designer.maven.utils;

import org.apache.maven.model.Dependency;

public class SortableDependency extends Dependency implements Comparable<SortableDependency> {

    @Override
    public int compareTo(SortableDependency o) {

        return getArtifactId().compareTo(o.getArtifactId());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getGroupId().hashCode();
        result = 31 * result + getArtifactId().hashCode();
        result = 31 * result + getVersion().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getClassifier().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof SortableDependency) {
            return false;
        }
        SortableDependency caseobj = (SortableDependency) obj;

        return (getGroupId() == caseobj.getGroupId() || (getGroupId() != null && getGroupId().equals(caseobj.getGroupId())))
                && (getArtifactId() == caseobj.getArtifactId()
                        || (getArtifactId() != null && getArtifactId().equals(caseobj.getArtifactId())))
                && (getVersion() == caseobj.getVersion() || (getVersion() != null && getVersion().equals(caseobj.getVersion())))
                && (getType() == caseobj.getType() || (getType() != null && getType().equals(caseobj.getType())))
                && (getClassifier() == caseobj.getClassifier()
                        || (getClassifier() != null && getClassifier().equals(caseobj.getClassifier())));
    }

}