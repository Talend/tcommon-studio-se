// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.routines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.joblet.model.JobletFactory;
import org.talend.designer.joblet.model.JobletProcess;

public class RoutinesUtilTest {

    @Test
    public void testSetInnerCodes() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        RoutinesUtil.setInnerCodes(property, ERepositoryObjectType.ROUTINESJAR);
        assertEquals(property.getAdditionalProperties().get("JAR_TYPE"), ERepositoryObjectType.ROUTINESJAR.name());
        RoutinesUtil.setInnerCodes(property, null);
        assertNull(property.getAdditionalProperties().get("JAR_TYPE"));
    }

    @Test
    public void testGetInnerCodeType() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        RoutinesUtil.setInnerCodes(property, ERepositoryObjectType.ROUTINESJAR);
        assertEquals(RoutinesUtil.getInnerCodeType(property), ERepositoryObjectType.ROUTINESJAR);
    }

    @Test
    public void testIsInnerCodes() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        RoutinesUtil.setInnerCodes(property, ERepositoryObjectType.ROUTINESJAR);
        assertTrue(RoutinesUtil.isInnerCodes(property));
        RoutinesUtil.setInnerCodes(property, null);
        assertFalse(RoutinesUtil.isInnerCodes(property));
    }

    @Test
    public void testGetCodesJarLabelByInnerCode() {
        RoutineItem innerRoutineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
        ItemState state = PropertiesFactory.eINSTANCE.createItemState();
        state.setPath("/Jar1");
        innerRoutineItem.setState(state);
        assertEquals(RoutinesUtil.getCodesJarLabelByInnerCode(innerRoutineItem), "Jar1");
    }

    @Test
    public void testGetRoutinesParametersFromJobInfo_Job() {
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        ProcessType processType = TalendFileFactory.eINSTANCE.createProcessType();
        ParametersType paramType = TalendFileFactory.eINSTANCE.createParametersType();
        processItem.setProcess(processType);
        processType.setParameters(paramType);
        RoutinesParameterType routinesParameterType = TalendFileFactory.eINSTANCE.createRoutinesParameterType();
        routinesParameterType.setId("1");
        routinesParameterType.setName("jar1");
        routinesParameterType.setType("ROUTINESJAR");
        processType.getParameters().getRoutinesParameter().add(routinesParameterType);

        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setId(ProxyRepositoryFactory.getInstance().getNextId());
        property.setLabel("RoutinesUtilTest_testGetRoutinesParametersFromJobInfo_Job_job1");
        property.setVersion("1.0");
        property.setItem(processItem);

        JobInfo jobInfo = new JobInfo(processItem, "Default");
        List<RoutinesParameterType> result = RoutinesUtil.getRoutinesParametersFromJobInfo(jobInfo);
        assertTrue(result.size() == 1);
        RoutinesParameterType resultType = result.get(0);
        assertEquals(resultType.getId(), routinesParameterType.getId());
        assertEquals(resultType.getName(), routinesParameterType.getName());
        assertEquals(resultType.getId(), routinesParameterType.getId());
    }

    @Test
    public void testGetRoutinesParametersFromJobInfo_Joblet() {
        JobletProcessItem jobletProcessItem = PropertiesFactory.eINSTANCE.createJobletProcessItem();
        JobletProcess jobletProcessType = JobletFactory.eINSTANCE.createJobletProcess();
        ParametersType paramType = TalendFileFactory.eINSTANCE.createParametersType();
        jobletProcessItem.setJobletProcess(jobletProcessType);
        jobletProcessType.setParameters(paramType);
        RoutinesParameterType routinesParameterType = TalendFileFactory.eINSTANCE.createRoutinesParameterType();
        routinesParameterType.setId("1");
        routinesParameterType.setName("jar1");
        routinesParameterType.setType("ROUTINESJAR");
        jobletProcessType.getParameters().getRoutinesParameter().add(routinesParameterType);

        Property jobletProperty = PropertiesFactory.eINSTANCE.createProperty();
        jobletProperty.setId(ProxyRepositoryFactory.getInstance().getNextId());
        jobletProperty.setLabel("RoutinesUtilTest_testGetRoutinesParametersFromJobInfo_Joblet_joblet1");
        jobletProperty.setVersion("1.0");
        jobletProperty.setItem(jobletProcessItem);
        JobInfo jobInfo = new JobInfo(jobletProperty, "Default");
        List<RoutinesParameterType> result = RoutinesUtil.getRoutinesParametersFromJobInfo(jobInfo);
        assertTrue(result.size() == 1);
        RoutinesParameterType resultType = result.get(0);
        assertEquals(resultType.getId(), routinesParameterType.getId());
        assertEquals(resultType.getName(), routinesParameterType.getName());
        assertEquals(resultType.getId(), routinesParameterType.getId());
    }

}
