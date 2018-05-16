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
package org.talend.repository.ui.wizards.metadata.table.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.repository.utils.URIHelper;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.ViewHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.model.emf.CwmResource;
import org.talend.utils.files.FileUtils;
import org.talend.utils.io.FilesUtils;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ConnectionUUIDHelper {

    private final DatabaseConnection baseConn;

    private DatabaseConnection originalConnection;

    private final File tempWorkFolder;

    public ConnectionUUIDHelper(DatabaseConnection baseConn) {
        super();
        this.baseConn = baseConn;
        this.tempWorkFolder = FileUtils.createTmpFolder("Database", "");//$NON-NLS-1$ //$NON-NLS-2$
    }

    public void clean() {
        if (tempWorkFolder != null) {
            FilesUtils.deleteFolder(tempWorkFolder, true);
        }
    }

    public boolean recordConnection() {
        if (baseConn != null && baseConn.eResource() != null && originalConnection == null) {
            FileOutputStream fos = null;
            FileInputStream fis = null;
            try {
                File tmpFile = new File(tempWorkFolder, System.currentTimeMillis() + FileExtensions.ITEM_FILE_SUFFIX);
                fos = new FileOutputStream(tmpFile);
                EmfHelper.saveResource(baseConn.eResource(), fos);

                fis = new FileInputStream(tmpFile);

                URI uri = URIHelper.convert(new Path(tmpFile.getAbsolutePath()));
                Resource tmpResourse = new CwmResource(uri); // for db
                EmfHelper.loadResource(tmpResourse, fis, null);
                originalConnection = (DatabaseConnection) EcoreUtil.getObjectByType(tmpResourse.getContents(),
                        ConnectionPackage.eINSTANCE.getDatabaseConnection());

                return true;
            } catch (Exception e) {
                ExceptionHandler.process(e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        //
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }
        return false;
    }

    public void resetUUID(DatabaseConnection itemConnection) {
        // need resource for UUID
        if (originalConnection == null || originalConnection.eResource() == null || itemConnection == null
                || itemConnection.eResource() == null) {
            return;
        }
        // checkObjectForSameUUID(anotherConnection,originalTmpConn); //for connection?

        // TaggedValue
        checkTaggedValues(itemConnection.getTaggedValue(), originalConnection.getTaggedValue());

        checkParameters(itemConnection.getParameters(), originalConnection.getParameters());

        // elements
        EList<ModelElement> itemOwnedElements = itemConnection.getOwnedElement();
        EList<ModelElement> originalOwnedElements = originalConnection.getOwnedElement();
        checkOwnedElements(itemOwnedElements, originalOwnedElements);

        // package in same level
        EList<Package> itemDataPackages = itemConnection.getDataPackage();
        EList<Package> originalDataPackages = originalConnection.getDataPackage();
        checkPackages(itemDataPackages, originalDataPackages);

    }

    private void checkObjectForSameUUID(EObject itemObject, EObject originalObject) {
        // have source
        if (itemObject != null && originalObject != null && itemObject.eResource() != null && originalObject.eResource() != null
                && !ResourceHelper.areSame(itemObject, originalObject)) { // only check UUID
            String uuid = ResourceHelper.getUUID(originalObject);
            ResourceHelper.setUUid(itemObject, uuid);
        }
    }

    private void checkParameters(EMap<String, String> itemParameters, EMap<String, String> originalParameters) {
        // TODO
    }

    private void checkPackages(List<? extends Package> itemPackages, List<? extends Package> originalPackages) {
        for (Package itemPackage : itemPackages) {
            for (Package originalPackage : originalPackages) {
                if (originalPackage.getName().equals(itemPackage.getName())) {
                    checkObjectForSameUUID(itemPackage, originalPackage);

                    // TaggedValue
                    checkTaggedValues(itemPackage.getTaggedValue(), originalPackage.getTaggedValue());

                    // Elements
                    EList<ModelElement> itemOwnedElements = itemPackage.getOwnedElement();
                    EList<ModelElement> originalOwnedElements = originalPackage.getOwnedElement();
                    checkOwnedElements(itemOwnedElements, originalOwnedElements);

                    break; // continue to check another item Package
                } // ignore others
            }
        }

    }

    private void checkOwnedElements(List<? extends ModelElement> itemOwnedElements,
            List<? extends ModelElement> originalOwnedElements) {
        // TdTable
        List<TdTable> itemTables = TableHelper.getTables(itemOwnedElements);
        List<TdTable> originalTables = TableHelper.getTables(originalOwnedElements);
        checkTables(itemTables, originalTables);

        // TdView
        List<TdView> itemViews = ViewHelper.getViews(itemOwnedElements);
        List<TdView> originalViews = ViewHelper.getViews(originalOwnedElements);
        checkViews(itemViews, originalViews);

        // schema
        List<Schema> itemSchemas = SchemaHelper.getSchemas(itemOwnedElements);
        List<Schema> originalSchemas = SchemaHelper.getSchemas(originalOwnedElements);
        checkPackages(itemSchemas, originalSchemas);

        // catalog
        List<Catalog> itemCatalogs = CatalogHelper.getCatalogs(itemOwnedElements);
        List<Catalog> originalCatalogs = CatalogHelper.getCatalogs(originalOwnedElements);
        checkPackages(itemCatalogs, originalCatalogs);
    }

    private void checkTaggedValues(List<TaggedValue> itemTaggedValue, List<TaggedValue> originalTaggedValue) {
        List<String> tags = new ArrayList<String>();

        Iterator<TaggedValue> itemIterator = itemTaggedValue.iterator();
        while (itemIterator.hasNext()) {
            TaggedValue itemTV = itemIterator.next();
            String itemTag = itemTV.getTag();
            if (tags.contains(itemTag)) { // FIXME existed same tag??
                itemIterator.remove();
                continue;
            }
            for (TaggedValue originalTV : originalTaggedValue) {
                if (itemTag.equals(originalTV.getTag())) {
                    tags.add(itemTag);
                    checkObjectForSameUUID(itemTV, originalTV);

                    // FIXME, need check others??
                    // itemTV.getStereotype()

                    break; // continue to check another item TaggedValue
                } // ignore others
            }
        }
    }

    private void checkTables(List<TdTable> itemTables, List<TdTable> originalTables) {
        for (TdTable itemTable : itemTables) {
            for (TdTable originalTable : originalTables) {
                // same name or label
                if (itemTable.getName() != null && itemTable.getName().equals(originalTable.getName())
                        || itemTable.getLabel().equals(originalTable.getLabel())) {
                    checkObjectForSameUUID(itemTable, originalTable);

                    // if same name ,should be same id also
                    itemTable.setId(originalTable.getId());

                    // TaggedValue
                    checkTaggedValues(itemTable.getTaggedValue(), originalTable.getTaggedValue());

                    // TdColumn
                    List<TdColumn> itemColumns = TableHelper.getColumns(itemTable);
                    List<TdColumn> originalColumns = TableHelper.getColumns(originalTable);
                    checkColumns(itemColumns, originalColumns);

                    break; // continue to check another item TdTable
                } // ignore others
            }
        }
    }

    private void checkViews(List<TdView> itemViews, List<TdView> originalViews) {
        for (TdView itemView : itemViews) {
            for (TdView originalView : originalViews) {
                // same name or label
                if (itemView.getName() != null && itemView.getName().equals(originalView.getName())
                        || itemView.getLabel().equals(originalView.getLabel())) {
                    checkObjectForSameUUID(itemView, originalView);

                    // TaggedValue
                    checkTaggedValues(itemView.getTaggedValue(), originalView.getTaggedValue());

                    // TdColumn
                    List<TdColumn> itemColumns = ViewHelper.getColumns(itemView);
                    List<TdColumn> originalColumns = ViewHelper.getColumns(originalView);
                    checkColumns(itemColumns, originalColumns);

                    break; // continue to check another item TdView
                } // ignore others
            }
        }
    }

    private void checkColumns(List<TdColumn> itemColumns, List<TdColumn> originalColumns) {
        for (TdColumn itemColumn : itemColumns) {
            for (TdColumn originalColumn : originalColumns) {
                if (itemColumn.getLabel().equals(originalColumn.getLabel())) {
                    checkObjectForSameUUID(itemColumn, originalColumn);

                    // TaggedValue
                    EList<TaggedValue> itemTaggedValue = itemColumn.getTaggedValue();
                    EList<TaggedValue> originalTaggedValue = originalColumn.getTaggedValue();
                    checkTaggedValues(itemTaggedValue, originalTaggedValue);

                    // TdSqlDataType
                    TdSqlDataType itemSqlDataType = itemColumn.getSqlDataType();
                    TdSqlDataType originalSqlDataType = originalColumn.getSqlDataType();
                    checkObjectForSameUUID(itemSqlDataType, originalSqlDataType);

                    // Expression
                    Expression itemInitialValue = itemColumn.getInitialValue();
                    Expression originalInitialValue = originalColumn.getInitialValue();
                    checkObjectForSameUUID(itemInitialValue, originalInitialValue);

                    break; // continue to check another item TdColumn
                } // ignore others
            }
        }
    }

}
