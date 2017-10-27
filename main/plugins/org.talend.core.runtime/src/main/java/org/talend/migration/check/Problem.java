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
package org.talend.migration.check;

/**
 * created by wchen on Dec 30, 2014 Detailled comment
 *
 */
public class Problem {

    private ProblemCategory category;

    private String problem;

    /**
     * Getter for category.
     * 
     * @return the category
     */
    public ProblemCategory getCategory() {
        return this.category;
    }

    /**
     * Sets the category.
     * 
     * @param category the category to set
     */
    public void setCategory(ProblemCategory category) {
        this.category = category;
    }

    /**
     * Getter for problem.
     * 
     * @return the problem
     */
    public String getProblem() {
        return this.problem;
    }

    /**
     * Sets the problem.
     * 
     * @param problem the problem to set
     */
    public void setProblem(String problem) {
        this.problem = problem;
    }

}
