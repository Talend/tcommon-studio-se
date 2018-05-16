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
package org.talend.core.hadoop;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryTypeProcessor;
import org.talend.repository.model.IRepositoryNode;

/**
 * created by ycbai on 2013-1-28 Detailled comment
 *
 */
public interface IHadoopClusterService extends IService {

    /**
     * DOC ycbai Comment method "containedByCluster".
     *
     * Estimate whether or not the hadoopConnection belongs to the hadoopClusterConnection.
     *
     * @param hadoopClusterConnection
     * @param hadoopConnection
     * @return
     */
    public boolean containedByCluster(Connection hadoopClusterConnection, Connection hadoopConnection);

    /**
     * DOC ycbai Comment method "getHadoopClusterType".
     *
     * Get the hadoop cluster repository type.
     *
     * @return
     */
    public ERepositoryObjectType getHadoopClusterType();

    /**
     * DOC ycbai Comment method "isHadoopClusterNode".
     *
     * Estimate whether or not the node is a hadoop cluster node.
     *
     * @param node
     * @return
     */
    public boolean isHadoopClusterNode(IRepositoryNode node);

    /**
     * DOC ycbai Comment method "isHadoopSubnode".
     *
     * Estimate whether or not the node is a subnode of a hadoop cluster.
     *
     * @param node
     * @return
     */
    public boolean isHadoopSubnode(IRepositoryNode node);

    /**
     *
     * DOC ycbai Comment method "isHadoopFolderNode".
     *
     * Estimate whether or not the node is a folder node of a hadoop cluster.
     *
     * @param node
     * @return
     */
    public boolean isHadoopFolderNode(IRepositoryNode node);

    /**
     * DOC ycbai Comment method "isHadoopClusterItem".
     *
     * Estimate whether or not the item is a hadoop cluster item.
     *
     * @param item
     * @return
     */
    public boolean isHadoopClusterItem(Item item);

    /**
     * DOC ycbai Comment method "isHadoopSubItem".
     *
     * Estimate whether or not the item is a subitem of a hadoop cluster item.
     *
     * @param item
     * @return
     */
    public boolean isHadoopSubItem(Item item);

    /**
     * DOC ycbai Comment method "isValidHadoopSubItem".
     *
     * Estimate whether or not the item is a valid subitem of a hadoop cluster item.
     *
     * @param item
     * @return
     */
    public boolean isValidHadoopSubItem(Item item);

    /**
     * DOC ycbai Comment method "isHadoopSubItem".
     * <p>
     * Estimate whether or not the item is hadoop subconnection item.
     *
     * @param item
     * @return
     */
    public boolean isHadoopSubItem2(Item item);

    /**
     * DOC ycbai Comment method "isInContextMode".
     * <p>
     * Estimate whether or not the connection is in context mode. <br>
     * Hadoop subconnections are in context mode even though it is not context mode itself if the hadoop cluster is in
     * context mode.
     *
     * @param connection
     * @return
     */
    public boolean isInContextMode(Connection connection);

    /**
     * DOC ycbai Comment method "getSubitemIdsOfHadoopCluster".
     *
     * Get subitem ids of hadoop cluster.
     *
     * @param item hadoop cluster item
     * @return
     */
    public List<String> getSubitemIdsOfHadoopCluster(Item item);

    public Item getHadoopClusterBySubitemId(String subItemId);

    public Item getHadoopClusterBySubitemId(Project project, String subItemId);

    /**
     * DOC ycbai Comment method "refreshCluster".
     *
     * Refresh this Hadoop Cluster.
     *
     * @param clusterId
     */
    public void refreshCluster(String clusterId);

    /**
     * DOC ycbai Comment method "getHadoopDbParameters".
     *
     * Get db connection(like hbase, hive) parameters by hadoop cluster.
     *
     * @param clusterId
     * @return
     */
    public Map<String, String> getHadoopDbParameters(String clusterId);

    /**
     * DOC ycbai Comment method "removeHadoopDbParameters".
     *
     * @param connection
     */
    public void removeHadoopDbParameters(DatabaseConnection connection);

    /**
     * DOC ycbai Comment method "copyHadoopCluster".
     *
     * @param sourceItem
     * @param path
     * @throws PersistenceException
     * @throws BusinessException
     */
    public void copyHadoopCluster(final Item sourceItem, final IPath path) throws PersistenceException, BusinessException;

    /**
     *
     * DOC ycbai Comment method "copyHadoopCluster".
     *
     * @param sourceItem
     * @param path
     * @param newName
     * @throws PersistenceException
     * @throws BusinessException
     */
    public void copyHadoopCluster(final Item sourceItem, final IPath path, String newName) throws PersistenceException,
            BusinessException;

    public Map<String, String> getHadoopCustomLibraries(String clusterId);

    public IRepositoryTypeProcessor getHadoopSubMultiRepTypeProcessor(String[] repositoryTypes);

    /**
     * DOC ycbai Comment method "hasDiffsFromClusterToProcess".
     *
     * <p>
     * Check whether there is any change of hadoop related parameters from Process to Hadoop cluster.
     * </p>
     *
     * @param hcConnection
     * @param process
     * @return true if there are some changes from them, otherwise return false.
     */
    public boolean hasDiffsFromClusterToProcess(Item item, IProcess process);

    public Connection getHadoopClusterConnectionBySubConnection(Connection hadoopSubConnection);

    public String getHadoopClusterProperties(Connection hadoopSubConnection);

    /**
     * DOC ycbai Comment method "getHadoopClusterItemById".
     *
     * @param id is the id of hadoop cluster connection or its subconnection.
     * @return the hadoop cluster item.
     */
    public Item getHadoopClusterItemById(String id);

    public String getCustomConfsJarName(String id);

    public String getCustomConfsJarName(String id, boolean createJarIfNotExist, boolean addExtraIds);

    public String getCustomConfsJarName(ConnectionItem connectionItem, boolean createJarIfNotExist, boolean addExtraIds);

    public void useCustomConfsJarIfNeeded(List<ModuleNeeded> modulesNeeded, String clusterId);

    public boolean useClouderaNavi(Connection hadoopSubConnection);

    public String getClouderaNaviUserName(Connection hadoopSubConnection);

    public String getClouderaNaviPassword(Connection hadoopSubConnection);

    public String getClouderaNaviUrl(Connection hadoopSubConnection);

    public String getClouderaNaviMetadataUrl(Connection hadoopSubConnection);

    public String getClouderaNaviClientUrl(Connection hadoopSubConnection);

    public boolean clouderaNaviAutoCommit(Connection hadoopSubConnection);

    public boolean clouderaNaviDisableSSL(Connection hadoopSubConnection);

    public boolean clouderaNaviDieOnError(Connection hadoopSubConnection);

    public String getRepositoryTypeOfHadoopSubItem(Item subItem);

    public boolean isUseDynamicConfJar(String id);

    public boolean updateConfJarsByContextGroup(ContextItem contextItem, Map<String, String> contextGroupRanamedMap);

}
