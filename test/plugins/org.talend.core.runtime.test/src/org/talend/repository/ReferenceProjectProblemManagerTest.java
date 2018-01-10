package org.talend.repository;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;

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
public class ReferenceProjectProblemManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCheckCycleReference() {
        Map<String, List<ProjectReference>> referenceMap = new HashMap<String, List<ProjectReference>>();

        List<ProjectReference> rList = new ArrayList<ProjectReference>();
        rList.add(getProjectReferenceInstance("R1", "master"));
        rList.add(getProjectReferenceInstance("R2", "master"));
        rList.add(getProjectReferenceInstance("R0", "master"));
        referenceMap.put("R", rList);

        List<ProjectReference> r1List = new ArrayList<ProjectReference>();
        r1List.add(getProjectReferenceInstance("R2", "master"));
        referenceMap.put("R1", r1List);

        assertTrue(ReferenceProjectProblemManager.checkCycleReference(referenceMap));

        List<ProjectReference> r0List = new ArrayList<ProjectReference>();
        r0List.add(getProjectReferenceInstance("R1", "master"));
        referenceMap.put("R0", r0List);
        assertTrue(ReferenceProjectProblemManager.checkCycleReference(referenceMap));

        r0List.add(getProjectReferenceInstance("R", "master"));
        assertTrue(!ReferenceProjectProblemManager.checkCycleReference(referenceMap));
    }

    @Test
    public void testCheckMoreThanOneBranch() throws BusinessException {
        Map<String, List<ProjectReference>> referenceMap = new HashMap<String, List<ProjectReference>>();
        List<ProjectReference> rList = new ArrayList<ProjectReference>();
        rList.add(getProjectReferenceInstance("R1", "master"));
        rList.add(getProjectReferenceInstance("R2", "master"));
        rList.add(getProjectReferenceInstance("R0", "master"));
        referenceMap.put("R", rList);

        List<ProjectReference> r1List = new ArrayList<ProjectReference>();
        r1List.add(getProjectReferenceInstance("R2", "branches/A1"));
        referenceMap.put("R1", r1List);

        thrown.expect(BusinessException.class);
        ReferenceProjectProblemManager.checkMoreThanOneBranch(referenceMap);
    }

    private ProjectReference getProjectReferenceInstance(String referenceLabel, String referenceBranch) {
        ProjectReference pr = PropertiesFactory.eINSTANCE.createProjectReference();
        pr.setReferencedBranch(referenceBranch);
        Project project = PropertiesFactoryImpl.eINSTANCE.createProject();
        project.setTechnicalLabel(referenceLabel);
        pr.setReferencedProject(project);
        return pr;
    }

}
