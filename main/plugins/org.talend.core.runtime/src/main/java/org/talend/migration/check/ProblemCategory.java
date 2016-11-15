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
package org.talend.migration.check;

public enum ProblemCategory {
    JAR_MISSING("Jar Missing"),
    COMPONENT_MISSING("Component Missing"),
    COMPILATION_ERROR("Compile Error"),
    RUN_TEST_CASE_FAILED("Test Case Failed"),
    JOBLET_MISSING("Joblet Missging");

    private String displayName;

    ProblemCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
