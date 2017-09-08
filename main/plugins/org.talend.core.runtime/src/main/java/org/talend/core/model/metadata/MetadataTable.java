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
package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.EReadOnlyComlumnPosition;

/**
 * Meta Data Table. Contains all the columns. <br/>
 * $Id: MetadataTable.java 46622 2010-08-11 10:04:57Z wliu $
 */
public class MetadataTable implements IMetadataTable, Cloneable {

    private String tableName;

    private List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();

    private List<IMetadataColumn> unusedColumns = new ArrayList<IMetadataColumn>();

    private List<String> originalColumns;

    private IMetadataConnection parent;

    private boolean readOnly = false;

    private String dbms;

    private String attachedConnector;

    private String readOnlyColumnPosition;

    private String label;

    private String id;

    private String comment;

    private Map<String, String> additionalProperties = new HashMap<String, String>();

    private boolean isRepository = false;

    private String tableType;

    @Override
    public String getReadOnlyColumnPosition() {
        return this.readOnlyColumnPosition;
    }

    @Override
    public void setReadOnlyColumnPosition(String readOnlyColumnPosition) {
        this.readOnlyColumnPosition = readOnlyColumnPosition;
    }

    @Override
    public String toString() {
        StringBuffer toReturn = new StringBuffer(getTableName() + ":"); //$NON-NLS-1$
        for (IMetadataColumn cur : listColumns) {
            toReturn.append(cur.toString() + " "); //$NON-NLS-1$
        }
        return toReturn.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.metadata.IMetadataTable#getTableName()
     */
    @Override
    public String getTableName() {
        return this.tableName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.metadata.IMetadataTable#setTableName(java.lang.String)
     */
    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<IMetadataColumn> getListUsedColumns() {
        return this.listColumns;
    }

    @Override
    public List<IMetadataColumn> getListUnusedColumns() {
        return this.unusedColumns;
    }

    @Override
    public void setUnusedColumns(List<IMetadataColumn> unusedColumns) {
        this.unusedColumns = unusedColumns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.metadata.IMetadataTable#getListColumns()
     */
    @Override
    public List<IMetadataColumn> getListColumns() {
        // List<IMetadataColumn> temp = new ArrayList<IMetadataColumn>();
        // for (IMetadataColumn column : this.listColumns) {
        // if (!column.isUsefulColumn()) {
        // temp.add(column);
        // }
        // }
        // return temp;
        return getListColumns(false);
        // return this.listColumns;
    }

    @Override
    public synchronized List<IMetadataColumn> getListColumns(boolean withUnselected) {
        Iterator<IMetadataColumn> it = this.listColumns.iterator();
        while (it.hasNext()) {
            IMetadataColumn column = it.next();
            if (!column.isUsefulColumn()) {
                this.unusedColumns.add(column);
                it.remove();
            }
        }
        Iterator<IMetadataColumn> it2 = this.unusedColumns.iterator();
        while (it2.hasNext()) {
            IMetadataColumn column = it2.next();
            if (column.isUsefulColumn()) {
                this.listColumns.add(column);
                it2.remove();
            }
        }
        if (withUnselected) {
            List<IMetadataColumn> temp = new ArrayList<IMetadataColumn>();
            temp.addAll(this.listColumns);
            temp.addAll(this.unusedColumns);
            if (isRepository && originalColumns != null) {
                Collections.sort(temp, new Comparator<IMetadataColumn>() {

                    @Override
                    public int compare(IMetadataColumn o1, IMetadataColumn o2) {
                        int index1 = originalColumns.indexOf(o1.getLabel());
                        int index2 = originalColumns.indexOf(o2.getLabel());
                        return index1 - index2;
                    }
                });
            }
            return temp;
        }
        return this.listColumns;
    }

    @Override
    public boolean isDynamicSchema() {
        return getDynamicColumn() != null;

    }

    @Override
    public IMetadataColumn getDynamicColumn() {
        for (IMetadataColumn column : listColumns) {
            if (column.getTalendType().equals("id_Dynamic")) {
                return column;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.metadata.IMetadataTable#setListColumns(Hashtable)
     */
    @Override
    public void setListColumns(List<IMetadataColumn> listColumns) {
        this.listColumns = listColumns;
    }

    @Override
    public IMetadataTable clone(boolean withCustoms) {
        IMetadataTable clonedMetadata = null;
        try {
            clonedMetadata = (IMetadataTable) super.clone();

            List<IMetadataColumn> clonedMetaColumns = new ArrayList<IMetadataColumn>();
            clonedMetadata.setListColumns(clonedMetaColumns);
            for (int i = 0; i < listColumns.size(); i++) {
                clonedMetaColumns.add(listColumns.get(i).clone(withCustoms));
            }
            List<IMetadataColumn> clonedMetaUnusedColumns = new ArrayList<IMetadataColumn>();
            clonedMetadata.setUnusedColumns(clonedMetaUnusedColumns);
            for (int i = 0; i < unusedColumns.size(); i++) {
                clonedMetaColumns.add(unusedColumns.get(i).clone(withCustoms));
            }
            if (originalColumns != null) {
                List<String> clonedOriginalColumns = new ArrayList<String>(originalColumns);
                clonedMetadata.setOriginalColumns(clonedOriginalColumns);

            }
            clonedMetadata.setTableName(this.getTableName());
            clonedMetadata.setLabel(this.getLabel());
            clonedMetadata.setAdditionalProperties(new HashMap<String, String>(additionalProperties));
        } catch (CloneNotSupportedException e) {
            // nothing
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        return clonedMetadata;
    }

    /**
     * cloned without custom columns by default.
     */
    @Override
    public IMetadataTable clone() {
        return clone(false);
    }

    /**
     * Note: for a table with custom columns, the order for the test is really important. It should be
     * outputMetadata.sameMetadataAs (inputMetadata).
     */
    @Override
    public boolean sameMetadataAs(IMetadataTable input, int options) {
        if (this == input) {
            return true;
        }
        if (input == null) {
            return false;
        }
        if (!(input instanceof IMetadataTable)) {
            return false;
        }
        List<IMetadataColumn> thisColumnListWithUnselected = this.getListColumns(true);
        List<IMetadataColumn> inputColumnListWithUnselected = input.getListColumns(true);
        if (thisColumnListWithUnselected == null) {
            if (inputColumnListWithUnselected != null) {
                return false;
            }
        } else {
            if (thisColumnListWithUnselected.size() == inputColumnListWithUnselected.size()) {
                // test if standard columns (no custom, or same input / output)
                for (int i = 0; i < inputColumnListWithUnselected.size(); i++) {
                    IMetadataColumn otherColumn = inputColumnListWithUnselected.get(i);
                    if (isRepository) {
                        boolean exist = false;
                        for (int j = 0; j < thisColumnListWithUnselected.size(); j++) {
                            IMetadataColumn myColumn = thisColumnListWithUnselected.get(j);
                            if (otherColumn.getLabel().equals(myColumn.getLabel())) {
                                exist = true;
                                if (!otherColumn.sameMetacolumnAs(myColumn, options)) {
                                    return false;
                                }
                                break;
                            }
                        }
                        if (!exist) {
                            return false;
                        }
                    } else {
                        IMetadataColumn myColumn = thisColumnListWithUnselected.get(i);
                        if (!otherColumn.sameMetacolumnAs(myColumn, options)) {
                            return false;
                        }
                    }
                }
            } else {
                // output should have at least all columns from input, but can add some others (custom)
                // custom columns are considered as columns who can be added to output

                List<IMetadataColumn> outputColumnsNotTested = new ArrayList<IMetadataColumn>(thisColumnListWithUnselected);

                // test that input columns are correctly propagated to output first
                // no matter if this one is custom or not (all custom must be propagated too)
                for (int i = 0; i < inputColumnListWithUnselected.size(); i++) {
                    IMetadataColumn inputColumn = inputColumnListWithUnselected.get(i);
                    IMetadataColumn myColumn = this.getColumn(inputColumn.getLabel());
                    outputColumnsNotTested.remove(myColumn);
                    if (!inputColumn.sameMetacolumnAs(myColumn, options)) {
                        return false;
                    }
                }

                // we tested already all input columns propagated to output
                // means normally there should be only possible custom columns remaining in the schema.
                // if one of the column remaining in output is not custom, then there is a problem of propagation
                for (IMetadataColumn column : outputColumnsNotTested) {
                    if (!column.isCustom()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean sameMetadataAs(IMetadataTable other) {
        return sameMetadataAs(other, IMetadataColumn.OPTIONS_IGNORE_DBCOLUMNNAME | IMetadataColumn.OPTIONS_IGNORE_DEFAULT
                | IMetadataColumn.OPTIONS_IGNORE_COMMENT);
    }

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    @Override
    public IMetadataConnection getParent() {
        return this.parent;
    }

    /**
     * Sets the parent.
     * 
     * @param parent the parent to set
     */
    @Override
    public void setParent(IMetadataConnection parent) {
        this.parent = parent;
    }

    @Override
    public IMetadataColumn getColumn(String columnName) {
        List<IMetadataColumn> withUnusedList = getListColumns(true);
        for (int i = 0; i < withUnusedList.size(); i++) {
            IMetadataColumn column = withUnusedList.get(i);
            if (column.getLabel().equals(columnName)) {
                return column;
            }
        }
        return null;
    }

    @Override
    public void sortCustomColumns() {
        List<IMetadataColumn> customColumns = new ArrayList<IMetadataColumn>();
        List<IMetadataColumn> noCustomColumns = new ArrayList<IMetadataColumn>();

        for (int i = 0; i < listColumns.size(); i++) {
            IMetadataColumn column = listColumns.get(i);
            if (column.isCustom()) {
                customColumns.add(column);
            }
        }

        listColumns.removeAll(customColumns);
        noCustomColumns.addAll(listColumns);

        Collections.sort(customColumns, new Comparator<IMetadataColumn>() {

            @Override
            public int compare(IMetadataColumn col1, IMetadataColumn col2) {
                return col1.getCustomId() - col2.getCustomId();
            }

        });

        listColumns.clear();
        if (this.readOnlyColumnPosition != null && this.readOnlyColumnPosition.equals(EReadOnlyComlumnPosition.TOP.getName())) {
            listColumns.addAll(customColumns);
            listColumns.addAll(noCustomColumns);
        } else {
            listColumns.addAll(noCustomColumns);
            listColumns.addAll(customColumns);
        }
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String getDbms() {
        return dbms;
    }

    @Override
    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    /**
     * Getter for attachedConnector.
     * 
     * @return the attachedConnector
     */
    @Override
    public String getAttachedConnector() {
        return attachedConnector;
    }

    /**
     * Sets the attachedConnector.
     * 
     * @param attachedConnector the attachedConnector to set
     */
    @Override
    public void setAttachedConnector(String attachedConnector) {
        this.attachedConnector = attachedConnector;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @Override
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public boolean isRepository() {
        return this.isRepository;
    }

    public void setRepository(boolean isRepository) {
        this.isRepository = isRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.IMetadataTable#getTableType()
     */
    @Override
    public String getTableType() {
        return this.tableType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.IMetadataTable#setTableType()
     */
    @Override
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    /**
     * Sets the originalColumns.
     * 
     * @param originalColumns the originalColumns to set
     */
    @Override
    public void setOriginalColumns(List<String> originalColumns) {
        this.originalColumns = originalColumns;
    }

    /**
     * Getter for originalColumns.
     * 
     * @return the originalColumns
     */
    @Override
    public List<String> getOriginalColumns() {
        return this.originalColumns;
    }
}
