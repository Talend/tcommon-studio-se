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
package org.talend.repository.ui.wizards.metadata.table.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.repository.utils.URIHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.model.emf.CwmResource;
import org.talend.utils.io.FilesUtils;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ConnectionUUIDHelperTest {

    protected static final String TEST_FOLDER = "resources/test";

    protected static final String TEST_DB_FOLDER = TEST_FOLDER + "/db";

    protected static final String TEST_DB_MYSQL_ITEM = TEST_DB_FOLDER + "/MySQL_0.1.item";

    protected static final String TEST_DB_MYSQL_DIFF_LABEL_ITEM = TEST_DB_FOLDER + "/MySQL_0.1-diffLabel.item";

    protected static final String TEST_DB_MYSQL_REMOVE_TABLE_ITEM = TEST_DB_FOLDER + "/MySQL_0.1-removeTable.item";

    protected File testBaseFolder;

    private DatabaseConnection originalItemConn;

    private File originalItemFile;

    private Resource originalItemResourse;

    private ConnectionUUIDHelper uuidHelper;

    @Before
    public void createTestFolder() throws IOException {
        testBaseFolder = File.createTempFile(this.getClass().getSimpleName(), "Junits");
        testBaseFolder.delete();
        testBaseFolder.mkdirs();

        // load test file
        File testDataFile = getTestDataFile(TEST_DB_MYSQL_ITEM);
        assertNotNull(testDataFile);
        originalItemFile = new File(testBaseFolder, testDataFile.getName());
        FilesUtils.copyFile(testDataFile, originalItemFile);

        URI originalItemURI = URIHelper.convert(new Path(originalItemFile.getAbsolutePath()));
        originalItemResourse = new CwmResource(originalItemURI);
        FileInputStream originalItemStream = null;
        try {
            originalItemStream = new FileInputStream(originalItemFile);
            EmfHelper.loadResource(originalItemResourse, originalItemStream, null);
            originalItemConn = (DatabaseConnection) EcoreUtil.getObjectByType(originalItemResourse.getContents(),
                    ConnectionPackage.eINSTANCE.getDatabaseConnection());
            assertNotNull(originalItemConn);
        } finally {
            if (originalItemStream != null) {
                originalItemStream.close();
            }
        }

        Set<MetadataTable> tables = ConnectionHelper.getTables(originalItemConn);
        assertEquals(tables.size(), 3);

        uuidHelper = new ConnectionUUIDHelper(originalItemConn);
        uuidHelper.recordConnection();
    }

    @After
    public void clearTestFolder() throws IOException {
        if (testBaseFolder != null) {
            FilesUtils.deleteFolder(testBaseFolder, true);
        }
        if (uuidHelper != null) {
            uuidHelper.clean();
        }
    }

    protected File getTestDataFile(String bundlePath) throws IOException {
        URL dataUrl = FileLocator.find(FrameworkUtil.getBundle(this.getClass()), new Path(bundlePath), null);
        if (dataUrl != null) {
            dataUrl = FileLocator.toFileURL(dataUrl);
        }

        File zipFile = new File(dataUrl.getFile());
        if (zipFile.exists()) {
            return zipFile;
        }
        return null;
    }

    private MetadataTable findTable(String name) {
        Set<MetadataTable> tables = ConnectionHelper.getTables(originalItemConn);

        MetadataTable table = null;
        for (MetadataTable t : tables) {
            if (t.getName().equals(name)) {
                table = t;
                break;
            }
        }
        return table;
    }

    @Test
    public void testCopiedTable() throws Exception {
        assertNotNull(originalItemConn);

        MetadataTable copyTable = findTable("tstat"); // must be last one, else the order is different
        assertNotNull(copyTable); // found

        Package parentPackage = PackageHelper.getParentPackage(copyTable);
        assertNotNull(parentPackage); // found
        boolean removed = parentPackage.getOwnedElement().remove(copyTable);
        assertTrue(removed);

        // add copied back
        MetadataTable copiedTable = EcoreUtil.copy(copyTable);
        copiedTable.setId("123456");
        parentPackage.getOwnedElement().add(copiedTable);

        uuidHelper.resetUUID(originalItemConn);

        // id was set back
        MetadataTable table = findTable("tstat");
        assertEquals(table.getId(), copyTable.getId()); // id changed back.
        assertEquals(table.getName(), copyTable.getName());
        assertEquals(table.getLabel(), copyTable.getLabel());

        File copiedFile = new File(testBaseFolder, "ABC" + System.currentTimeMillis() + FileExtensions.ITEM_FILE_SUFFIX);
        FileOutputStream copiedFos = new FileOutputStream(copiedFile);
        originalItemResourse.save(copiedFos, null);

        File testDataFile = getTestDataFile(TEST_DB_MYSQL_ITEM);
        assertNotNull(testDataFile);
        assertEquals(FilesUtils.getChecksumAlder32(testDataFile), FilesUtils.getChecksumAlder32(copiedFile));
    }

    @Test
    public void testDeleteTable() throws Exception {
        assertNotNull(originalItemConn);

        MetadataTable removedTable = findTable("tstat");
        assertNotNull(removedTable); // found

        Package parentPackage = PackageHelper.getParentPackage(removedTable);
        assertNotNull(parentPackage); // found
        boolean removed = parentPackage.getOwnedElement().remove(removedTable);
        assertTrue(removed);

        Set<MetadataTable> removedTables = ConnectionHelper.getTables(originalItemConn);
        assertEquals(removedTables.size(), 2); // 2 left

        uuidHelper.resetUUID(originalItemConn);

        File removedFile = new File(testBaseFolder, "ABC" + System.currentTimeMillis() + FileExtensions.ITEM_FILE_SUFFIX);
        FileOutputStream removedFos = new FileOutputStream(removedFile);
        originalItemResourse.save(removedFos, null);

        File testRemovedTableFile = getTestDataFile(TEST_DB_MYSQL_REMOVE_TABLE_ITEM);
        assertNotNull(testRemovedTableFile);
        assertEquals(FilesUtils.getChecksumAlder32(testRemovedTableFile), FilesUtils.getChecksumAlder32(removedFile));
    }

    @Test
    public void testDiffTableLabel() throws Exception {
        assertNotNull(originalItemConn);

        MetadataTable changeTable = findTable("tflow");
        assertNotNull(changeTable); // found

        changeTable.setId("1234567");
        changeTable.setLabel("tflow2");

        uuidHelper.resetUUID(originalItemConn);
        changeTable = findTable("tflow");
        assertEquals(changeTable.getId(), "_cwhh8Ck6Eea_f9a5xt9fUQ"); // id changed back.
        assertEquals(changeTable.getLabel(), "tflow2");// label keep the change

        File changedFile = new File(testBaseFolder, "ABC" + System.currentTimeMillis() + FileExtensions.ITEM_FILE_SUFFIX);
        FileOutputStream changedFos = new FileOutputStream(changedFile);
        originalItemResourse.save(changedFos, null);

        File testDataFile = getTestDataFile(TEST_DB_MYSQL_DIFF_LABEL_ITEM);
        assertEquals(FilesUtils.getChecksumAlder32(testDataFile), FilesUtils.getChecksumAlder32(changedFile));
    }

}
