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
package org.talend.core.model.process;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractExternalNode extends AbstractNode implements IExternalNode {

    private IODataComponentContainer ioDataContainer;

    protected ImageDescriptor screenshot = null;

    protected INode originalNode;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractNode#getExternalNode()
     */
    @Override
    public IExternalNode getExternalNode() {
        return this;
    }

    @Override
    public List<Problem> getProblems() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.core.model.process.IExternalNode#setIODataComponents(org.talend.core.model.components.
     * IODataComponentContainer)
     */
    @Override
    public void setIODataComponents(IODataComponentContainer ioDatacontainer) {
        this.ioDataContainer = ioDatacontainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getIODataComponents()
     */
    @Override
    public IODataComponentContainer getIODataComponents() {
        return this.ioDataContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#metataChanged(org.talend.core.model.components.IODataComponent)
     */
    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        for (ColumnNameChanged col : dataComponent.getColumnNameChanged()) {
            // System.out.println(" -> " + col + " " + connectionToApply); //$NON-NLS-1$ //$NON-NLS-2$
            this.renameMetadataColumnName(connectionToApply, col.getOldName(), col.getNewName());
        }
    }

    @Override
    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        for (ColumnNameChanged col : dataComponent.getColumnNameChanged()) {
            // System.out.println(" -> " + col + " " + connectionToApply); //$NON-NLS-1$ //$NON-NLS-2$
            this.renameMetadataColumnName(connectionToApply, col.getOldName(), col.getNewName());
        }
    }

    @Override
    public void metadataOutputChanged(IMetadataTable currentMetadata) {

    }

    protected abstract void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#isRunRefSubProcessAtStart(java.lang.String)
     */
    @Override
    public boolean isRunRefSubProcessAtStart(String connectionName) {
        return true;
    }

    @Override
    public ImageDescriptor getScreenshot() {
        return this.screenshot;
    }

    @Override
    public void setScreenshot(ImageDescriptor screenshot) {
        this.screenshot = screenshot;
    }

    @Override
    public INode getOriginalNode() {
        return this.originalNode;
    }

    @Override
    public void setOriginalNode(INode originalNode) {
        this.originalNode = originalNode;
    }

    @Override
    public AbstractExternalData getExternalEmfData() {
        return null;
    }

    @Override
    public void buildExternalData(AbstractExternalData abstractData) {

    }

    @Override
    public INode getJobletNode() {
        return null;
    }

    @Override
    public void setExternalEmfData(AbstractExternalData externalEmfData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setInternalMapperModel(IMapperModel mapperModel) {
        // TODO Auto-generated method stub
    }

    @Override
    public IMapperModel getInternalMapperModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> checkNeededRoutines(List<String> possibleRoutines, String additionalString) {
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.process.IExternalNode#connectionStatusChanged(org.talend.core.model.process.EConnectionType
     * , java.lang.String)
     */
    @Override
    public void connectionStatusChanged(EConnectionType newValue, String connectionToApply) {
        // TODO Auto-generated method stub

    }
}
