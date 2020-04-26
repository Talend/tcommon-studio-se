package org.talend.designer.maven.utils;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.model.Dependency;

public class SortableDependency extends Dependency implements Comparable<SortableDependency> {

    private static final long serialVersionUID = -6295226523517981508L;

    /**
     * dependencies in testcase/routines/beans no need to be assembled in final zip.
     */
    private boolean isAssemblyOptional;

    @Override
    public int compareTo(SortableDependency o) {
        int compare = getArtifactId().compareTo(o.getArtifactId());
        if (compare == 0) {
            // FIXME according to Maven official Doc for dependencies:
            // https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html
            // when pom has duplicate version of dependencies, Maven should take the first one
            // but in practice(maven 3.5.3 embedded), it always take the last one
            // we only need job dependencies in final job zip rather than testcase's
            // so we always put the latest job dependency at the bottom
            // if maven fix it in future, we need to reverse the order as well.
            if (isAssemblyOptional && !o.isAssemblyOptional) {
                return -1;
            }
            if (!isAssemblyOptional && o.isAssemblyOptional) {
                return 1;
            }
            return new ComparableVersion(getVersion()).compareTo(new ComparableVersion(o.getVersion()));
        }
        return compare;
    }

    public boolean isAssemblyOptional() {
        return isAssemblyOptional;
    }

    public void setAssemblyOptional(boolean isAssemblyOptional) {
        this.isAssemblyOptional = isAssemblyOptional;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getGroupId() == null ? 0 : getGroupId().hashCode());
        result = 31 * result + (getArtifactId() == null ? 0 : getArtifactId().hashCode());
        result = 31 * result + (getVersion() == null ? 0 : getVersion().hashCode());
        result = 31 * result + (getType() == null ? 0 : getType().hashCode());
        result = 31 * result + (getClassifier() == null ? 0 : getClassifier().hashCode());
        result = 31 * result + (isAssemblyOptional() ? 1231 : 1237);
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
        if (!(obj instanceof SortableDependency)) {
            return false;
        }
        SortableDependency caseobj = (SortableDependency) obj;

        return (getGroupId() == caseobj.getGroupId() || (getGroupId() != null && getGroupId().equals(caseobj.getGroupId())))
                && (getArtifactId() == caseobj.getArtifactId()
                        || (getArtifactId() != null && getArtifactId().equals(caseobj.getArtifactId())))
                && (getVersion() == caseobj.getVersion() || (getVersion() != null && getVersion().equals(caseobj.getVersion())))
                && (getType() == caseobj.getType() || (getType() != null && getType().equals(caseobj.getType())))
                && (getClassifier() == caseobj.getClassifier()
                        || (getClassifier() != null && getClassifier().equals(caseobj.getClassifier())))
                && (isAssemblyOptional() == caseobj.isAssemblyOptional());
    }

}