package org.talend.core.model.process;

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
import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.ProjectManager;

public class ProcessUtilsTest {

    @Test
    public void testGetPureItemId() {
        String jobId = "_rHnrstwXEeijXfdWFqSaEA";
        assertEquals(jobId, ProcessUtils.getPureItemId(jobId));
        String projectLabel = "PROJECT721";
        assertEquals(jobId, ProcessUtils.getPureItemId(projectLabel + ProcessUtils.PROJECT_ID_SEPARATOR + jobId));
    }

    @Test
    public void testGetProjectLabelFromItemId() {
        String jobId = "_rHnrstwXEeijXfdWFqSaEA";
        assertNull(ProcessUtils.getProjectLabelFromItemId(jobId));
        String projectLabel = "PROJECT721";
        assertEquals(projectLabel,
                ProcessUtils.getProjectLabelFromItemId(projectLabel + ProcessUtils.PROJECT_ID_SEPARATOR + jobId));
    }

    @Test
    public void testGetProjectProcessId() {
        String jobId = "_rHnrstwXEeijXfdWFqSaEA";
        assertEquals(jobId, ProcessUtils.getProjectProcessId(null, jobId));
        assertEquals(jobId, ProcessUtils.getProjectProcessId("", jobId));
        assertEquals(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel() + ProcessUtils.PROJECT_ID_SEPARATOR + jobId,
                ProcessUtils.getProjectProcessId(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), jobId));
        String projectLabel = "PROJECT721";
        assertEquals(projectLabel + ProcessUtils.PROJECT_ID_SEPARATOR + jobId,
                ProcessUtils.getProjectProcessId(projectLabel, jobId));
    }

    @Test
    public void testGetProjectProcessLabel() {
        String nodeLabel = "TRunJob";
        assertEquals(nodeLabel, ProcessUtils.getProjectProcessLabel(null, nodeLabel));
        assertEquals(nodeLabel, ProcessUtils.getProjectProcessLabel("", nodeLabel));
        assertEquals(nodeLabel, ProcessUtils
                .getProjectProcessLabel(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), nodeLabel));
        String projectLabel = "PROJECT721";
        assertEquals(nodeLabel, ProcessUtils
                .getProjectProcessLabel(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), nodeLabel));
        assertEquals(projectLabel + ProcessUtils.PROJECT_ID_SEPARATOR + nodeLabel,
                ProcessUtils.getProjectProcessLabel(projectLabel, nodeLabel));
    }

    @Test
    public void testIsSameProperty() {
        String jobId = "_rHnrstwXEeijXfdWFqSaEA";
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setId(jobId);
        property.setLabel("test1");
        property.setVersion(VersionUtils.DEFAULT_VERSION);

        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();
        item.setProcess(process);
        item.setProperty(property);

        String projectLabel = "PROJECT721";
        String jobId2 = ProcessUtils.getProjectProcessId(projectLabel, jobId);
        assertTrue(ProcessUtils.isSameProperty(property, jobId));
        assertFalse(ProcessUtils.isSameProperty(property, jobId2));

        assertTrue(ProcessUtils.isSameProperty(projectLabel, jobId, jobId2));
        assertFalse(ProcessUtils.isSameProperty(null, jobId, jobId2));
        assertFalse(ProcessUtils.isSameProperty("", jobId, jobId2));
        assertTrue(ProcessUtils.isSameProperty(null, jobId2, jobId));

        assertTrue(ProcessUtils.isSameProperty(jobId, jobId2, true));
        assertTrue(ProcessUtils.isSameProperty(jobId, jobId, true));
        assertTrue(ProcessUtils.isSameProperty(jobId2, jobId2, true));
        assertFalse(ProcessUtils.isSameProperty(jobId, jobId2, false));
    }
}
