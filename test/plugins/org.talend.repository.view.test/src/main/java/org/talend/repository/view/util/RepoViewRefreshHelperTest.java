// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.view.util;

import java.io.ByteArrayInputStream;

import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepoViewRefreshHelperTest {

    static RepoViewRefreshHelper helper;

    @BeforeClass
    public static void prepare() {
        helper = new RepoViewRefreshHelper();
    }

    @AfterClass
    public static void cleanup() {
        helper = null;
    }

    @Test
    public void test_getValidResourceFile_blankPath() {
        IFile file = helper.getValidResourceFile(null);
        Assert.assertNull(file);

        file = helper.getValidResourceFile("");
        Assert.assertNull(file);

        file = helper.getValidResourceFile("   ");
        Assert.assertNull(file);
    }

    @Test
    public void test_getValidResourceFile_nonExistedPath() {
        IFile file = helper.getValidResourceFile(ProjectManager.getInstance().getCurrentProject().getLabel() + "/mytest");
        Assert.assertNull(file);
    }

    @Test
    public void test_getValidResourceFile_jobPath() throws PersistenceException, CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();

        final IProject project = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject());
        final IFolder jobFolder = project.getFolder(ERepositoryObjectType.PROCESS.getFolder());
        Assert.assertTrue("The folder should be existed: " + jobFolder, jobFolder.exists());

        final IFile abcFile = jobFolder.getFile("abc.txt");
        if (!abcFile.exists()) {
            abcFile.create(new ByteArrayInputStream("abc".getBytes()), Resource.FORCE, monitor);
        }
        try {
            IPath filePath = abcFile.getLocation().makeRelativeTo(ResourcesPlugin.getWorkspace().getRoot().getLocation());

            IFile file = helper.getValidResourceFile(filePath.toString());
            Assert.assertNotNull("It should be valid:" + filePath, file);
        } finally {
            if (abcFile.exists()) {
                abcFile.delete(true, monitor);
            }
        }
    }

    @Test
    public void test_getValidResourceFile_componentsPath() throws PersistenceException, CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();

        final IProject project = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject());
        final IFolder compFolder = project.getFolder(ERepositoryObjectType.COMPONENTS.getFolder());
        Assert.assertTrue("The folder should be existed: " + compFolder, compFolder.exists());

        final IFile abcFile = compFolder.getFile("abc.txt");
        if (!abcFile.exists()) {
            abcFile.create(new ByteArrayInputStream("abc".getBytes()), Resource.FORCE, monitor);
        }
        try {
            IPath filePath = abcFile.getLocation().makeRelativeTo(ResourcesPlugin.getWorkspace().getRoot().getLocation());

            IFile file = helper.getValidResourceFile(filePath.toString());
            Assert.assertNull("should be invalid:" + filePath, file);
        } finally {
            if (abcFile.exists()) {
                abcFile.delete(true, monitor);
            }
        }
    }

    @Test
    public void test_getValidResourceFile_tempPath() throws PersistenceException, CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();

        final IProject project = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject());
        final IFolder tempFolder = project.getFolder("temp");
        Assert.assertTrue("The folder should be existed: " + tempFolder, tempFolder.exists());

        final IFile abcFile = tempFolder.getFile("abc.txt");
        if (!abcFile.exists()) {
            abcFile.create(new ByteArrayInputStream("abc".getBytes()), Resource.FORCE, monitor);
        }
        try {
            IPath filePath = abcFile.getLocation().makeRelativeTo(ResourcesPlugin.getWorkspace().getRoot().getLocation());

            IFile file = helper.getValidResourceFile(filePath.toString());
            Assert.assertNull("should be invalid:" + filePath, file);
        } finally {
            if (abcFile.exists()) {
                abcFile.delete(true, monitor);
            }
        }
    }
}