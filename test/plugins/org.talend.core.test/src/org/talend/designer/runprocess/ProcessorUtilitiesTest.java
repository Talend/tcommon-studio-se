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
package org.talend.designer.runprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.runprocess.shadow.ObjectElementParameter;
import org.talend.core.runtime.process.LastGenerationInfo;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProcessorUtilitiesTest {

    LastGenerationInfo generationInfo;

    @Before
    public void before() {
        generationInfo = LastGenerationInfo.getInstance();
        generationInfo.clean();
    }

    private void assertGenerationInfoUsingInit(JobInfo jobInfo, JobInfo childJobInfo) {
        assertFalse(generationInfo.isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertFalse(generationInfo.isUseDynamic(childJobInfo.getJobId(), childJobInfo.getJobVersion()));

        assertFalse(generationInfo.isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertFalse(generationInfo.isUsePigUDFs(childJobInfo.getJobId(), childJobInfo.getJobVersion()));

        assertFalse(generationInfo.isUseRules(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertFalse(generationInfo.isUseRules(childJobInfo.getJobId(), childJobInfo.getJobVersion()));
    }

    private JobInfo createJobInfo(String id, String version, String name) {
        final JobInfo jobInfo = new JobInfo(id, "Junit", version);
        jobInfo.setJobName(name);
        return jobInfo;
    }

    private JobInfo createMainJobInfo() {
        return createJobInfo("abc123xyz456", "0.3", "TestJunit");
    }

    private JobInfo createChildJobInfo() {
        return createJobInfo("abc123xyz789", "0.2", "TestJunitChild");
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_checkUseXXX_trueChildren() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();

        assertGenerationInfoUsingInit(jobInfo, childJobInfo);

        // set false for main job, true for children job
        generationInfo.setUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion(), false);
        generationInfo.setUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion(), false);
        generationInfo.setUseRules(jobInfo.getJobId(), jobInfo.getJobVersion(), false);
        generationInfo.setUseDynamic(childJobInfo.getJobId(), childJobInfo.getJobVersion(), true);
        generationInfo.setUsePigUDFs(childJobInfo.getJobId(), childJobInfo.getJobVersion(), true);
        generationInfo.setUseRules(childJobInfo.getJobId(), childJobInfo.getJobVersion(), true);

        ProcessorUtilities.setGenerationInfoWithChildrenJob(null, jobInfo, childJobInfo);

        assertTrue(generationInfo.isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertTrue(generationInfo.isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertTrue(generationInfo.isUseRules(jobInfo.getJobId(), jobInfo.getJobVersion()));
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_checkUseXXX_falseChildren() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();

        assertGenerationInfoUsingInit(jobInfo, childJobInfo);

        // set false for main job, false for children job
        generationInfo.setUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion(), false);
        generationInfo.setUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion(), false);
        generationInfo.setUseRules(jobInfo.getJobId(), jobInfo.getJobVersion(), false);
        generationInfo.setUseDynamic(childJobInfo.getJobId(), childJobInfo.getJobVersion(), false);
        generationInfo.setUsePigUDFs(childJobInfo.getJobId(), childJobInfo.getJobVersion(), false);
        generationInfo.setUseRules(childJobInfo.getJobId(), childJobInfo.getJobVersion(), false);

        ProcessorUtilities.setGenerationInfoWithChildrenJob(null, jobInfo, childJobInfo);

        assertFalse(generationInfo.isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertFalse(generationInfo.isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertFalse(generationInfo.isUseRules(jobInfo.getJobId(), jobInfo.getJobVersion()));
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_checkUseXXX_falseChildren_trueMain() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();

        assertGenerationInfoUsingInit(jobInfo, childJobInfo);

        // set true for main job, false for children job
        generationInfo.setUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion(), true);
        generationInfo.setUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion(), true);
        generationInfo.setUseRules(jobInfo.getJobId(), jobInfo.getJobVersion(), true);
        generationInfo.setUseDynamic(childJobInfo.getJobId(), childJobInfo.getJobVersion(), false);
        generationInfo.setUsePigUDFs(childJobInfo.getJobId(), childJobInfo.getJobVersion(), false);
        generationInfo.setUseRules(childJobInfo.getJobId(), childJobInfo.getJobVersion(), false);

        ProcessorUtilities.setGenerationInfoWithChildrenJob(null, jobInfo, childJobInfo);

        // if true, will be true always
        assertTrue(generationInfo.isUseDynamic(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertTrue(generationInfo.isUsePigUDFs(jobInfo.getJobId(), jobInfo.getJobVersion()));
        assertTrue(generationInfo.isUseRules(jobInfo.getJobId(), jobInfo.getJobVersion()));
    }

    private void assertEmptyModules(JobInfo jobInfo) {
        assertTrue(generationInfo.getModulesNeededWithSubjobPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()).isEmpty());
        assertTrue(generationInfo.getRoutinesNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()).isEmpty());
        assertTrue(generationInfo.getPigudfNeededPerJob(jobInfo.getJobId(), jobInfo.getJobVersion()).isEmpty());
    }

    private INode createSubJobNode(boolean dynamicJob, boolean independent) {
        TestFakeNode node = new TestFakeNode();
        node.addElementParameter(new ObjectElementParameter("USE_DYNAMIC_JOB", dynamicJob));
        node.addElementParameter(new ObjectElementParameter("USE_INDEPENDENT_PROCESS", independent));
        return node;
    }

    private void initChildrenJobModules(final JobInfo childJobInfo) {
        Set<ModuleNeeded> subjobModules = new HashSet<ModuleNeeded>();
        subjobModules.add(new ModuleNeeded("ABC", "", true, "mvn:org.talend.libraries/slf4j-log4j12-1.7.2/6.0.0"));
        generationInfo.setModulesNeededWithSubjobPerJob(childJobInfo.getJobId(), childJobInfo.getJobVersion(), subjobModules);
        generationInfo.setRoutinesNeededWithSubjobPerJob(childJobInfo.getJobId(), childJobInfo.getJobVersion(),
                new HashSet<String>(Arrays.asList(new String[] { "core-1.0.jar", "abc_2.1.jar" })));
        generationInfo.setPigudfNeededWithSubjobPerJob(childJobInfo.getJobId(), childJobInfo.getJobVersion(),
                new HashSet<String>(Arrays.asList(new String[] { "pigudf-0.2.jar" })));
    }

    private void assertJobModules(JobInfo jobInfo) {
        final Set<ModuleNeeded> modules = generationInfo.getModulesNeededWithSubjobPerJob(jobInfo.getJobId(),
                jobInfo.getJobVersion());
        assertEquals(1, modules.size());
        final ModuleNeeded module1 = modules.iterator().next();
        assertEquals("ABC", module1.getContext());
        assertEquals("slf4j-log4j12-1.7.2.jar", module1.getModuleName());
        assertEquals("mvn:org.talend.libraries/slf4j-log4j12-1.7.2/6.0.0/jar", module1.getMavenUri());
        assertTrue(module1.isRequired());

        final Set<String> routineModules = generationInfo.getRoutinesNeededWithSubjobPerJob(jobInfo.getJobId(),
                jobInfo.getJobVersion());
        assertEquals(2, routineModules.size());
        assertTrue(routineModules.contains("core-1.0.jar"));
        assertTrue(routineModules.contains("abc_2.1.jar"));

        final Set<String> pigudfModules = generationInfo.getPigudfNeededWithSubjobPerJob(jobInfo.getJobId(),
                jobInfo.getJobVersion());
        assertEquals(1, pigudfModules.size());
        assertTrue(pigudfModules.contains("pigudf-0.2.jar"));
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_nullNode() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();

        assertEmptyModules(jobInfo);
        assertEmptyModules(childJobInfo);

        initChildrenJobModules(childJobInfo);
        ProcessorUtilities.setGenerationInfoWithChildrenJob(null, jobInfo, childJobInfo);

        // empty for main
        assertEmptyModules(jobInfo);
        assertJobModules(childJobInfo);
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_nullParam() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();
        final TestFakeNode node = new TestFakeNode();

        assertEmptyModules(jobInfo);
        assertEmptyModules(childJobInfo);

        initChildrenJobModules(childJobInfo);
        ProcessorUtilities.setGenerationInfoWithChildrenJob(node, jobInfo, childJobInfo);

        // contain children modules
        assertJobModules(jobInfo);
        assertJobModules(childJobInfo);
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_normal() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();
        final INode subjobNode = createSubJobNode(false, false);

        assertEmptyModules(jobInfo);
        assertEmptyModules(childJobInfo);

        initChildrenJobModules(childJobInfo);
        ProcessorUtilities.setGenerationInfoWithChildrenJob(subjobNode, jobInfo, childJobInfo);

        // contain children modules
        assertJobModules(childJobInfo);
        assertJobModules(jobInfo);
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_usingDynamicJob() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();
        final INode subjobNode = createSubJobNode(true, false);

        assertEmptyModules(jobInfo);
        assertEmptyModules(childJobInfo);

        initChildrenJobModules(childJobInfo);
        ProcessorUtilities.setGenerationInfoWithChildrenJob(subjobNode, jobInfo, childJobInfo);

        // empty for main
        assertEmptyModules(jobInfo);
        assertJobModules(childJobInfo);
    }

    @Test
    public void testSetGenerationInfoWithChildrenJob_independentJob() {
        final JobInfo jobInfo = createMainJobInfo();
        final JobInfo childJobInfo = createChildJobInfo();
        final INode subjobNode = createSubJobNode(false, true);

        assertEmptyModules(jobInfo);
        assertEmptyModules(childJobInfo);

        initChildrenJobModules(childJobInfo);
        ProcessorUtilities.setGenerationInfoWithChildrenJob(subjobNode, jobInfo, childJobInfo);

        // empty for main
        assertEmptyModules(jobInfo);
        assertJobModules(childJobInfo);
    }

    @Test
    public void test_hasMetadataDynamic_noNodes() {
        IProcess proc = mock(IProcess.class);
        when(proc.getGeneratingNodes()).thenReturn(Collections.emptyList());

        assertFalse("No any node in job, not metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));
    }

    @Test
    public void test_hasMetadataDynamic_GenericComp() {
        IProcess proc = mock(IProcess.class);

        INode genericNode = mock(INode.class);
        List nodes = new ArrayList();
        nodes.add(genericNode);
        when(proc.getGeneratingNodes()).thenReturn(nodes);

        IComponent comp = mock(IComponent.class);
        when(genericNode.getComponent()).thenReturn(comp);
        when(comp.getComponentType()).thenReturn(EComponentType.GENERIC);

        assertTrue("Has one generic component of node in job, should be metadata dynamic",
                ProcessorUtilities.hasMetadataDynamic(proc, null));
    }

    @Test
    public void test_hasMetadataDynamic_noDBNode() {
        IProcess proc = mock(IProcess.class);

        List nodes = new ArrayList();

        INode normalNode = mock(INode.class);
        nodes.add(normalNode);
        when(proc.getGeneratingNodes()).thenReturn(nodes);

        IComponent comp = mock(IComponent.class);
        when(normalNode.getComponent()).thenReturn(comp);
        when(comp.getComponentType()).thenReturn(EComponentType.EMF);

        // empty parameters
        when(normalNode.getElementParameters()).thenReturn(Collections.emptyList());
        assertFalse("no DB node in job, not metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

        List params = new ArrayList();
        when(normalNode.getElementParameters()).thenReturn(params);

        IElementParameter typeParam = mock(IElementParameter.class);
        params.add(typeParam);

        // not TYPE parameter
        when(typeParam.getName()).thenReturn("ABC");
        assertFalse("no DB node in job, not metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

        // not TEXT field
        when(typeParam.getName()).thenReturn("TYPE");
        when(typeParam.getFieldType()).thenReturn(EParameterFieldType.BUTTON);
        assertFalse("no DB node in job, not metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

        // null value
        when(typeParam.getFieldType()).thenReturn(EParameterFieldType.TEXT);
        assertFalse("no DB node in job, not metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

        // empty value
        when(typeParam.getValue()).thenReturn("");
        assertFalse("no DB node in job, not metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));
    }

    @Test
    public void test_hasMetadataDynamic_BDNodeWithoutDynamicType() {
        IProcess proc = mock(IProcess.class);

        List nodes = new ArrayList();

        INode dbNode = mock(INode.class);
        nodes.add(dbNode);
        when(proc.getGeneratingNodes()).thenReturn(nodes);

        IComponent comp = mock(IComponent.class);
        when(dbNode.getComponent()).thenReturn(comp);
        when(comp.getComponentType()).thenReturn(EComponentType.EMF);

        List params = new ArrayList();
        when(dbNode.getElementParameters()).thenReturn(params);

        // TYPE parameter
        IElementParameter typeParam = mock(IElementParameter.class);
        params.add(typeParam);
        when(typeParam.getName()).thenReturn("TYPE");
        when(typeParam.getFieldType()).thenReturn(EParameterFieldType.TEXT);
        when(typeParam.getValue()).thenReturn("MySQL");

        // empty table list
        when(dbNode.getMetadataList()).thenReturn(Collections.emptyList());
        assertFalse("DB node in job with out metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

        List metadataList = new ArrayList<>();
        when(dbNode.getMetadataList()).thenReturn(metadataList);

        IMetadataTable table = mock(IMetadataTable.class);
        metadataList.add(table);

        // empty table
        when(table.getListColumns()).thenReturn(Collections.emptyList());
        assertFalse("DB node in job with out metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

        List columnsList = new ArrayList<>();
        when(table.getListColumns()).thenReturn(columnsList);

        IMetadataColumn column1 = mock(IMetadataColumn.class);
        columnsList.add(column1);

        IMetadataColumn column2 = mock(IMetadataColumn.class);
        columnsList.add(column2);

        // non-dynamic
        when(column1.getTalendType()).thenReturn(JavaTypesManager.INTEGER.getId());
        when(column2.getTalendType()).thenReturn(JavaTypesManager.STRING.getId());
        assertFalse("DB node in job with out metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));

    }

    @Test
    public void test_hasMetadataDynamic_DynamicTalendType() {
        IProcess proc = mock(IProcess.class);

        List nodes = new ArrayList();

        INode dbNode = mock(INode.class);
        nodes.add(dbNode);
        when(proc.getGeneratingNodes()).thenReturn(nodes);

        IComponent comp = mock(IComponent.class);
        when(dbNode.getComponent()).thenReturn(comp);
        when(comp.getComponentType()).thenReturn(EComponentType.EMF);

        List params = new ArrayList();
        when(dbNode.getElementParameters()).thenReturn(params);

        // TYPE parameter
        IElementParameter typeParam = mock(IElementParameter.class);
        params.add(typeParam);
        when(typeParam.getName()).thenReturn("TYPE");
        when(typeParam.getFieldType()).thenReturn(EParameterFieldType.TEXT);
        when(typeParam.getValue()).thenReturn("MySQL");

        List metadataList = new ArrayList<>();
        when(dbNode.getMetadataList()).thenReturn(metadataList);

        IMetadataTable table = mock(IMetadataTable.class);
        metadataList.add(table);

        List columnsList = new ArrayList<>();
        when(table.getListColumns()).thenReturn(columnsList);

        IMetadataColumn column1 = mock(IMetadataColumn.class);
        columnsList.add(column1);

        IMetadataColumn column2 = mock(IMetadataColumn.class);
        columnsList.add(column2);

        // non-dynamic
        when(column1.getTalendType()).thenReturn(JavaTypesManager.INTEGER.getId());
        when(column2.getTalendType()).thenReturn("id_Dynamic");
        assertTrue("DB node in job. shoud be metadata dynamic", ProcessorUtilities.hasMetadataDynamic(proc, null));
    }
}
