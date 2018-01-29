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
package org.talend.repository.example.viewer.content.example;

import org.talend.repository.example.viewer.node.ExampleRootNode;

/**
 * created by sgandon on 1 août 2012 Detailled comment
 * 
 */
public class JobOnlyWithAnAContentProvider extends JobWithXLableContentProvider {

    public static final Object ROOT = new ExampleRootNode() {

        @Override
        public String toString() {
            return "Job With an A"; //$NON-NLS-1$
        }

    };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.example.JobWithXLableContentProvider#getRoot()
     */
    @Override
    protected Object getRoot() {
        return ROOT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.example.JobWithXLableContentProvider#validJob(java.lang.Object)
     */
    @Override
    protected boolean validJob(Object potentialJob) {
        return !jobTester.isJobOnlyWithAnA(potentialJob);
    }

}
