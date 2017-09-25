// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.aether.selector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;

/**
 * ExclusionDependencySelector which support regular expressions
 */
public class DynamicExclusionDependencySelector implements DependencySelector {

    private final Exclusion[] exclusions;

    private int hashCode;

    private Map<String, Pattern> patternMap = new HashMap<String, Pattern>();

    public DynamicExclusionDependencySelector() {
        exclusions = new Exclusion[0];
    }

    public DynamicExclusionDependencySelector(Collection<Exclusion> exclusions) {
        if ((exclusions != null) && (!exclusions.isEmpty())) {
            TreeSet<Exclusion> sorted = new TreeSet(ExclusionComparator.INSTANCE);
            sorted.addAll(exclusions);
            this.exclusions = (sorted.toArray(new Exclusion[sorted.size()]));
        } else {
            this.exclusions = new Exclusion[0];
        }
    }

    private DynamicExclusionDependencySelector(Exclusion[] exclusions) {
        this.exclusions = exclusions;
    }

    @Override
    public boolean selectDependency(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();
        for (Exclusion exclusion : exclusions) {
            if (matches(exclusion, artifact)) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(Exclusion exclusion, Artifact artifact) {
        String artifactId = exclusion.getArtifactId();
        if (StringUtils.isNotEmpty(artifactId)) {
            if (!matches(exclusion.getArtifactId(), artifact.getArtifactId())) {
                return false;
            }
        }

        String groupId = exclusion.getGroupId();
        if (StringUtils.isNotEmpty(groupId)) {
            if (!matches(exclusion.getGroupId(), artifact.getGroupId())) {
                return false;
            }
        }

        String extension = exclusion.getExtension();
        if (StringUtils.isNotEmpty(extension)) {
            if (!matches(exclusion.getExtension(), artifact.getExtension())) {
                return false;
            }
        }

        String classifier = exclusion.getClassifier();
        if (StringUtils.isNotEmpty(classifier)) {
            if (!matches(exclusion.getClassifier(), artifact.getClassifier())) {
                return false;
            }
        }

        return true;
    }

    private boolean matches(String pattern, String value) {
        if (pattern.startsWith("^")) { //$NON-NLS-1$
            Pattern ptn = getPattern(pattern);
            Matcher matcher = ptn.matcher(value);
            return matcher.matches();
        } else {
            return ("*".equals(pattern)) || (pattern.equals(value)); //$NON-NLS-1$
        }
    }

    private Pattern getPattern(String pattern) {
        Pattern ptn = patternMap.get(pattern);

        if (ptn == null) {
            ptn = Pattern.compile(pattern);
            patternMap.put(pattern, ptn);
        }

        return ptn;
    }

    @Override
    public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
        Dependency dependency = context.getDependency();
        Collection<Exclusion> exclusions = dependency != null ? dependency.getExclusions() : null;
        if ((exclusions == null) || (exclusions.isEmpty())) {
            return this;
        }

        Exclusion[] merged = this.exclusions;
        int count = merged.length;
        for (Exclusion exclusion : exclusions) {
            int index = Arrays.binarySearch(merged, exclusion, ExclusionComparator.INSTANCE);
            if (index < 0) {
                index = -(index + 1);
                if (count >= merged.length) {
                    Exclusion[] tmp = new Exclusion[merged.length + exclusions.size()];
                    System.arraycopy(merged, 0, tmp, 0, index);
                    tmp[index] = exclusion;
                    System.arraycopy(merged, index, tmp, index + 1, count - index);
                    merged = tmp;
                } else {
                    System.arraycopy(merged, index, merged, index + 1, count - index);
                    merged[index] = exclusion;
                }
                count++;
            }
        }
        if (merged == this.exclusions) {
            return this;
        }
        if (merged.length != count) {
            Exclusion[] tmp = new Exclusion[count];
            System.arraycopy(merged, 0, tmp, 0, count);
            merged = tmp;
        }

        DynamicExclusionDependencySelector newSelector = new DynamicExclusionDependencySelector(merged);

        if (patternMap != null && !patternMap.isEmpty()) {
            newSelector.patternMap.putAll(patternMap);
        }

        return newSelector;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((null == obj) || (!getClass().equals(obj.getClass()))) {
            return false;
        }

        DynamicExclusionDependencySelector that = (DynamicExclusionDependencySelector) obj;
        return Arrays.equals(exclusions, that.exclusions);
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            int hash = getClass().hashCode();
            hash = hash * 31 + Arrays.hashCode(exclusions);
            hashCode = hash;
        }
        return hashCode;
    }

    private static class ExclusionComparator implements Comparator<Exclusion> {

        static final ExclusionComparator INSTANCE = new ExclusionComparator();

        @Override
        public int compare(Exclusion e1, Exclusion e2) {
            if (e1 == null) {
                return e2 == null ? 0 : 1;
            }
            if (e2 == null) {
                return -1;
            }
            int rel = e1.getArtifactId().compareTo(e2.getArtifactId());
            if (rel == 0) {
                rel = e1.getGroupId().compareTo(e2.getGroupId());
                if (rel == 0) {
                    rel = e1.getExtension().compareTo(e2.getExtension());
                    if (rel == 0) {
                        rel = e1.getClassifier().compareTo(e2.getClassifier());
                    }
                }
            }
            return rel;
        }
    }
}
