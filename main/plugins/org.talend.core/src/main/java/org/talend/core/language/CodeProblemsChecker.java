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
package org.talend.core.language;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.talend.core.model.process.Problem;
import org.talend.designer.codegen.IAloneProcessNodeConfigurer;

public abstract class CodeProblemsChecker implements ICodeProblemsChecker {

    private List<Problem> problems;

    private MultiValueMap multiValueMap = new MultiValueMap();

    private List<Problem> nodeProblems = new ArrayList<Problem>();

    public List<Problem> checkProblemsFromKey(String key, IAloneProcessNodeConfigurer nodeConfigurer) {
        List<Problem> nodePros = checkProblems(nodeConfigurer);
        updateNodeProblems(nodePros, key);
        return nodePros;
    }

    public void updateNodeProblems(List<Problem> nodePros, String key) {
        nodeProblems.clear();
        if (nodePros != null) {
            nodeProblems.addAll(nodePros);
        }
        if (nodePros == null) {
            nodePros = null;
            setProblems(null);
        } else {
            setProblems(new ArrayList<Problem>(nodePros));
            for (Iterator iter = nodePros.iterator(); iter.hasNext();) {
                Problem problem = (Problem) iter.next();
                if (key == null && problem.getKey() != null || !key.equals(problem.getKey())) {
                    iter.remove();
                }
            }
            if (nodePros.size() == 0) {
                nodePros = null;
            } else {
                nodePros = new ArrayList<Problem>(nodePros);
            }
        }
    }

    public List<Problem> checkProblemsForErrorMark(String key, IAloneProcessNodeConfigurer nodeConfigurer) {
        String proKey = ""; //$NON-NLS-1$
        if (key != null) {
            int indeMark = key.indexOf(":"); //$NON-NLS-1$

            if (indeMark > 0) {
                proKey = key.substring(0, key.indexOf(":")); //$NON-NLS-1$
            } else {
                proKey = key;
            }
        } else {
            proKey = key;
        }

        // List<Problem> nodeProblems = checkProblems(nodeConfigurer);
        if (nodeProblems == null) {
            nodeProblems = null;
            setProblems(null);
        } else {
            setProblems(new ArrayList<Problem>(nodeProblems));
            for (Iterator iter = nodeProblems.iterator(); iter.hasNext();) {
                Problem problem = (Problem) iter.next();

                if (problem.getKey() != null) {
                    int inde = problem.getKey().indexOf(":"); //$NON-NLS-1$
                    String problemKey = ""; //$NON-NLS-1$
                    if (inde > 0) {
                        problemKey = problem.getKey().substring(0, inde);
                    } else {
                        problemKey = problem.getKey();
                    }
                    if (key == null && problem.getKey() != null || !proKey.equals(problemKey)) {
                        iter.remove();
                    }
                } else {
                    if (key == null && problem.getKey() != null || !proKey.equals(problem.getKey())) {
                        iter.remove();
                    }
                }
            }
            if (nodeProblems.size() == 0) {
                nodeProblems = null;
            } else {
                nodeProblems = new ArrayList<Problem>(nodeProblems);
            }
        }
        return nodeProblems;
    }

    public List<Problem> getProblemsFromKey(String key) {
        List<Problem> list = (List<Problem>) multiValueMap.get(key);
        return list;
    }

    protected List<Problem> getProblems() {
        return this.problems;
    }

    protected void setProblems(List<Problem> problems) {
        this.problems = problems;
        multiValueMap.clear();
        if (problems != null) {
            for (Problem problem : problems) {
                multiValueMap.put(problem.getKey(), problem);
            }
        }
    }

    public List<Problem> getNodeProblems() {
        return this.nodeProblems;
    }

}
