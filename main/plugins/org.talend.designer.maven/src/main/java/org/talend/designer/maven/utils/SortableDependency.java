package org.talend.designer.maven.utils;

import org.apache.maven.model.Dependency;

public class SortableDependency extends Dependency implements Comparable<SortableDependency> {

    @Override
    public int compareTo(SortableDependency o) {

        return getArtifactId().compareTo(o.getArtifactId());
    }

}