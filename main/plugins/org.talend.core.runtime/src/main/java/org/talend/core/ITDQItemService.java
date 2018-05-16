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
package org.talend.core;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface ITDQItemService extends ITDQService {

    /**
     * DOC bZhou Comment method "getVersion".
     * 
     * This function is to get the version for a specified item, it's used as additional functionality to listItem
     * command in the CommandLine, e.g. listItem -m
     * 
     * @param item
     * @return
     */
    public String getVersion(TDQItem item);

    /**
     * DOC bZhou Comment method "changeItemStatus".
     * 
     * This function is to change the status in one item, it used the specified status constant to set the value. It's
     * used for the changeItemStatus command in the CommandLine.
     * 
     * @param newStatus
     * @param item
     * @throws PersistenceException
     */
    public void changeItemStatus(String newStatus, Item item) throws PersistenceException;

    /**
     * DOC bZhou Comment method "changeVersions".
     * 
     * This function is to change the version for a item. it's used for the changeVersions command in the CommandLine.
     * 
     * @param newVersion
     * @param item
     * @throws Exception
     */
    public void changeVersions(String newVersion, Item item) throws Exception;

    /**
     * DOC bZhou Comment method "exportItems".
     * 
     * This function is to export items in current workspace to the destination location. it's used for the exportItems
     * command in the CommandLine.
     * 
     * @param file
     * @param tdqItems
     * @param isExportAll
     * @param withDependencies
     * @param nullProgressMonitor
     */
    public void exportItems(File file, List<Item> tdqItems, boolean isExportAll, boolean withDependencies,
            NullProgressMonitor nullProgressMonitor);

    /**
     * DOC bZhou Comment method "importItems".
     * 
     * Import items from out source, this is used for the importItems command in CommandLine.
     * 
     * @param sourceFile
     * @param validItems
     * @param overwrite
     * @param nullProgressMonitor
     */
    public void importItems(File sourceFile, List<Item> validItems, boolean overwrite, NullProgressMonitor nullProgressMonitor);

    /**
     * Invoked by GenerateGrammarController class, and return a file will be created in routines
     * 
     * DOC ytao
     * 
     * @param node, properties of component
     * @return
     */
    public File fileCreatedInRoutines(INode node, String className) throws Exception;

    /**
     * 
     * zshen create GrammerFile to temp location.
     * 
     * @param node same as fileCreatedInRoutines
     * @return
     * @throws Exception
     */
    public IFile createGrammerFile(INode node) throws Exception;

    /**
     * 
     * zshen Comment method "createGrammerFile".
     * 
     * @param rules the list which save the attribute about rule table.
     * @param folderName the .g file will be generate under it.
     * @return
     * @throws Exception
     */
    public IFile createGrammerFile(List<Map<String, String>> rules, String folderName) throws Exception;

    /**
     * 
     * zshen Comment method "deleteGrammerFile".
     * 
     * @param folderName the floder name wich will be delete.
     * @return
     */
    public boolean deleteGrammerFile(String folderName);

    /**
     * DOC sizhaoliu Comment method "getReportGenerationResources".
     * 
     * @param items
     * @return
     */
    public boolean hasProcessItemDependencies(Collection<Item> items);

    /**
     * DOC sizhaoliu Comment method "getReportGenerationResources".
     * 
     * @param items
     * @return
     */
    public Collection<IRepositoryViewObject> getProcessItemDependencies(Collection<Item> items);
}
