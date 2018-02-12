package org.talend.core;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoreServiceTest {
    
    private boolean withFilter = false;
    
    private CoreService service;

    private ITalendProcessJavaProject talendProcessJavaProject;

    private FilenameFilter filter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            if (XmlUtil.isXMLFile(name)) {
                return true;
            }
            return false;
        }
    };

    @Before
    public void setUp() throws Exception {
        service = new CoreService();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            talendProcessJavaProject = runProcessService.getTempJavaProject();
            //TODO create a temp job code project
        }
    }

    @Test
    public void testASyncMappingsFileFromSystemToProject() throws Exception {
        withFilter = false;
        
        String sysMappingFolder = MetadataTalendType.getSystemForderURLOfMappingsFile().getPath();
        IFolder projectMappingFolder = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject())
                .getFolder(MetadataTalendType.PROJECT_MAPPING_FOLDER);

        service.syncMappingsFileFromSystemToProject();

        modifyTargetFolder(projectMappingFolder);

        service.syncMappingsFileFromSystemToProject();

        validateConsistence(sysMappingFolder, projectMappingFolder.getLocation().toPortableString());
    }

    @Test
    public void testBSynchronizeMapptingXML() throws Exception {
        withFilter = true;
        
        IFolder resourceMappingFolder = talendProcessJavaProject.getResourceSubFolder(null, JavaUtils.JAVA_XML_MAPPING);
        String projectMappingFolder = MetadataTalendType.getProjectForderURLOfMappingsFile().getPath();

        service.synchronizeMapptingXML(talendProcessJavaProject);

        modifyTargetFolder(resourceMappingFolder);

        service.synchronizeMapptingXML(talendProcessJavaProject);

        validateConsistence(projectMappingFolder, resourceMappingFolder.getLocation().toPortableString());
    }

    @Test
    public void testCSyncLog4jSettings() throws CoreException, IOException {
        IRunProcessService runProcessService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        String log4jStrFromSettings = runProcessService.getTemplateStrFromPreferenceStore("log4jContent");

        IFile tmpFile = talendProcessJavaProject.getTempFolder().getFile("tempLog4j.xml");
        if (!tmpFile.exists()) {
            ByteArrayInputStream in = new ByteArrayInputStream(log4jStrFromSettings.getBytes());
            tmpFile.create(in, true, null);
            in.close();
        }
        log4jStrFromSettings = getFileContent(tmpFile);
        tmpFile.delete(true, null);

        service.syncLog4jSettings(talendProcessJavaProject);

        IFolder resourceFolder = talendProcessJavaProject.getExternalResourcesFolder();
        IFile log4jFile = resourceFolder.getFile("log4j.xml");
        if (log4jFile.exists()) {
            String content = "test modification";
            InputStream in = new ByteArrayInputStream(content.getBytes());
            log4jFile.setContents(in, true, false, null);
        }

        service.syncLog4jSettings(talendProcessJavaProject);

        String log4jStrFromResouce = getFileContent(log4jFile);
        
        assertEquals(log4jStrFromSettings, log4jStrFromResouce);

    }

    private String getFileContent(IFile file) throws CoreException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getContents()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String log4jStrFromResouce = stringBuilder.toString();
        bufferedReader.close();
        return log4jStrFromResouce;
    }

    private void modifyTargetFolder(IFolder folder) throws CoreException {
        String fileName1 = "mapping_Access.xml";
        IFile file1 = folder.getFile(withFilter? fileName1.toLowerCase() : fileName1);
        assertTrue(file1.exists());
        file1.delete(true, null);
        String fileName2 = "mapping_AS400.xml";
        IFile file2 = folder.getFile(withFilter? fileName2.toLowerCase() : fileName2);
        assertTrue(file2.exists());
        String content = "test modification";
        InputStream in = new ByteArrayInputStream(content.getBytes());
        file2.setContents(in, true, false, null);
    }

    private void validateConsistence(String folder1, String folder2) throws IOException {
        File dir1 = new File(folder1);
        File dir2 = new File(folder2);
        File[] subFiles1 = withFilter ? dir1.listFiles(filter) : dir1.listFiles();
        File[] subFiles2 = withFilter ? dir2.listFiles(filter) : dir2.listFiles();
        assertTrue(subFiles1.length == subFiles2.length);
        for (File subFile1 : subFiles1) {
            boolean isFound = false;
            long file1CRC = FilesUtils.getChecksumAlder32(subFile1);
            String subFile1Name = withFilter ? service.getTargetName(subFile1) : subFile1.getName();
            for (File subFile2 : subFiles2) {
                if (subFile2.getName().equals(subFile1Name)) {
                    isFound = true;
                    long file2CRC = FilesUtils.getChecksumAlder32(subFile2);
                    assertEquals(file1CRC, file2CRC);
                }
            }
            assertTrue(isFound);
        }
    }

}
