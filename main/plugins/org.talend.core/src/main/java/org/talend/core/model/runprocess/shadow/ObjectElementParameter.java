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
package org.talend.core.model.runprocess.shadow;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.properties.Item;

/**
 * Simple Text implementation of IElementParameter. <br/>
 * 
 * $Id: TextElementParameter.java 387 2006-11-10 08:34:31 +0000 (ven., 10 nov. 2006) nrousseau $
 * 
 */
public class ObjectElementParameter implements IElementParameter {

    private String name;

    private Object value;

    private String[] listItemsDisplayCodeName;

    private boolean contextMode;

    private RGB color;

    private RGB backgroundColor;

    private boolean raw;

    private boolean enable = true;

    /**
     * Constructs a new TextElementParameter.
     */
    public ObjectElementParameter(String name, Object value) {
        super();

        this.name = name;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getCategory()
     */
    @Override
    public EComponentCategory getCategory() {
        return EComponentCategory.MAIN;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getExtension()
     */
    public String getExtension() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getField()
     */
    @Override
    public EParameterFieldType getFieldType() {
        return EParameterFieldType.TABLE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsID()
     */
    @Override
    public String[] getListItemsValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsValue()
     */
    @Override
    public String[] getListItemsDisplayName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getMetadataType()
     */
    public String getMetadataType() {
        return "String"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNbLines()
     */
    @Override
    public int getNbLines() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNumRow()
     */
    @Override
    public int getNumRow() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getValue()
     */
    @Override
    public Object getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getVariableName()
     */
    @Override
    public String getVariableName() {
        return "__" + name + "__"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRequired()
     */
    @Override
    public boolean isRequired() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setCategory(org.talend
     * .core.model.process.EComponentCategory)
     */
    @Override
    public void setCategory(EComponentCategory cat) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDisplayName(java.lang .String)
     */
    @Override
    public void setDisplayName(String s) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setExtension(java.lang .String)
     */
    public void setExtension(String extension) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setField(org.talend.core .model.process.EParameterFieldType)
     */
    @Override
    public void setFieldType(EParameterFieldType type) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsID(java.lang .String[])
     */
    public void setListItemsValue(String[] list) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsValue(java .lang.String[])
     */
    @Override
    public void setListItemsDisplayName(String[] list) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setMetadataType(org.talend
     * .core.model.metadata.EMetadataType)
     */
    public void setMetadataType(String metadataType) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setName(java.lang.String)
     */
    @Override
    public void setName(String s) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNbLines(int)
     */
    @Override
    public void setNbLines(int nbLines) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNumRow(int)
     */
    @Override
    public void setNumRow(int numRow) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRequired(boolean)
     */
    @Override
    public void setRequired(boolean required) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setShow(boolean)
     */
    @Override
    public void setShow(boolean show) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValue(java.lang.Object )
     */
    @Override
    public void setValue(Object o) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDefaultClosedListValue
     * (org.talend.core.model.temp.ECodeLanguage)
     */
    @Override
    public Object getDefaultClosedListValue() {
        return null;
    }

    @Override
    public void setDefaultClosedListValue(Object o) {
    }

    @Override
    public void setListItemsValue(Object[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getRepositoryValue()
     */
    @Override
    public String getRepositoryValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryValue(java .lang.String)
     */
    @Override
    public void setRepositoryValue(String repositoryValue) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRepositoryValueUsed()
     */
    @Override
    public boolean isRepositoryValueUsed() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryValueUsed (boolean)
     */
    @Override
    public void setRepositoryValueUsed(boolean repositoryUsed) {
    }

    @Override
    public String[] getListRepositoryItems() {
        return null;
    }

    @Override
    public void setListRepositoryItems(String[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setShowIf(java.lang.String )
     */
    @Override
    public void setShowIf(String showIf) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getShowIf()
     */
    @Override
    public String getShowIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNotShowIf(java.lang .String)
     */
    @Override
    public void setNotShowIf(String notShowIf) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNotShowIf()
     */
    @Override
    public String getNotShowIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#testIfShow(java.util. List)
     */
    @Override
    public boolean isShow(List<? extends IElementParameter> listParam) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsDisplayCodeName
     * (org.talend.core.model.temp.ECodeLanguage)
     */
    @Override
    public String[] getListItemsDisplayCodeName() {
        return listItemsDisplayCodeName;
    }

    @Override
    public void setListItemsDisplayCodeName(String[] list) {
        listItemsDisplayCodeName = list;
    }

    @Override
    public String[] getListItemsNotShowIf() {
        return null;
    }

    @Override
    public String[] getListItemsShowIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isShow(java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotShowIf (java.lang.String[])
     */
    @Override
    public void setListItemsNotShowIf(String[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsShowIf(java .lang.String[])
     */
    @Override
    public void setListItemsShowIf(String[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDefaultValues()
     */
    @Override
    public List<IElementParameterDefaultValue> getDefaultValues() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDefaultValues(java .util.List)
     */
    @Override
    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValueToDefault(java .util.List)
     */
    @Override
    public void setValueToDefault(List<? extends IElementParameter> listParam) {
    }

    @Override
    public int getIndexOfItemFromList(String item) {
        return 0;
    }

    @Override
    public IElement getElement() {
        return null;
    }

    @Override
    public void setElement(IElement element) {
    }

    @Override
    public boolean isBasedOnSchema() {
        return false;
    }

    @Override
    public void setBasedOnSchema(boolean basedOnSchema) {

    }

    @Override
    public String getFilter() {
        return null;
    }

    @Override
    public void setFilter(String filter) {

    }

    @Override
    public boolean isNoCheck() {
        return false;
    }

    @Override
    public void setNoCheck(boolean noCheck) {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void setContext(String context) {

    }

    @Override
    public Map<String, IElementParameter> getChildParameters() {
        return null;
    }

    @Override
    public IElementParameter getParentParameter() {
        return null;
    }

    @Override
    public void setParentParameter(IElementParameter parentParameter) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getGroup()
     */
    @Override
    public String getGroup() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setGroup(java.lang.String )
     */
    @Override
    public void setGroup(String groupName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getGroupDisplayName()
     */
    @Override
    public String getGroupDisplayName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setGroupDisplayName(java .lang.String)
     */
    @Override
    public void setGroupDisplayName(String groupDisplayName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getLinkedRepositoryItem()
     */
    public Item getLinkedRepositoryItem() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLinkedRepositoryItem
     * (org.talend.core.model.properties.Item)
     */
    public void setLinkedRepositoryItem(Item item) {

    }

    /**
     * Getter for contextMode.
     * 
     * @return the contextMode
     */
    @Override
    public boolean isContextMode() {
        return this.contextMode;
    }

    /**
     * Sets the contextMode.
     * 
     * @param contextMode the contextMode to set
     */
    @Override
    public void setContextMode(boolean contextMode) {
        this.contextMode = contextMode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getLabelFromRepository()
     */
    @Override
    public String getLabelFromRepository() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLabelFromRepository (java.lang.String)
     */
    @Override
    public void setLabelFromRepository(String label) {

    }

    /**
     * Getter for color.
     * 
     * @return the color
     */
    @Override
    public RGB getColor() {
        return this.color;
    }

    /**
     * Sets the color.
     * 
     * @param color the color to set
     */
    @Override
    public void setColor(RGB color) {
        this.color = color;
    }

    /**
     * Getter for backgroundColor.
     * 
     * @return the backgroundColor
     */
    @Override
    public RGB getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the backgroundColor.
     * 
     * @param backgroundColor the backgroundColor to set
     */
    @Override
    public void setBackgroundColor(RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isBasedOnSubjobStarts()
     */
    @Override
    public boolean isBasedOnSubjobStarts() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setBasedOnSubjobStarts (boolean)
     */
    @Override
    public void setBasedOnSubjobStarts(boolean basedOnSubjobStarts) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isDynamicSettings()
     */
    @Override
    public boolean isDynamicSettings() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDynamicSettings(boolean )
     */
    @Override
    public void setDynamicSettings(boolean dynamicSettings) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsNotReadOnlyIf()
     */
    @Override
    public String[] getListItemsNotReadOnlyIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsReadOnlyIf()
     */
    @Override
    public String[] getListItemsReadOnlyIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNotReadOnlyIf()
     */
    @Override
    public String getNotReadOnlyIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getReadOnlyIf()
     */
    @Override
    public String getReadOnlyIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly(java.util.List)
     */
    @Override
    public boolean isReadOnly(List<? extends IElementParameter> listParam) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly(java.lang.String, java.lang.String,
     * java.util.List)
     */
    @Override
    public boolean isReadOnly(String conditionReadOnlyIf, String conditionNotReadOnlyIf,
            List<? extends IElementParameter> listParams) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotReadOnlyIf(java.lang.String[])
     */
    @Override
    public void setListItemsNotReadOnlyIf(String[] list) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsReadOnlyIf(java.lang.String[])
     */
    @Override
    public void setListItemsReadOnlyIf(String[] list) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNotReadOnlyIf(java.lang.String)
     */
    @Override
    public void setNotReadOnlyIf(String notShowIf) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setReadOnlyIf(java.lang.String)
     */
    @Override
    public void setReadOnlyIf(String showIf) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isColumnsBasedOnSchema()
     */
    @Override
    public boolean isColumnsBasedOnSchema() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setColumnsBasedOnSchema(boolean)
     */
    @Override
    public void setColumnsBasedOnSchema(boolean columnsBasedOnSchema) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isNoContextAssist()
     */
    @Override
    public boolean isNoContextAssist() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNoContextAssist(boolean)
     */
    @Override
    public void setNoContextAssist(boolean enable) {

    }

    @Override
    public IElementParameter getClone() {
        final IElementParameter clone = new ObjectElementParameter(this.name, this.value);

        clone.setCategory(this.getCategory());
        clone.setName(this.getName());
        clone.setFieldType(this.getFieldType());
        clone.setDisplayName(this.getDisplayName());
        clone.setValue(this.getValue());

        return clone;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getMaxlength()
     */
    @Override
    public int getMaxlength() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRequired(java.util.List)
     */
    @Override
    public boolean isRequired(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getRepositoryProperty()
     */
    @Override
    public String getRepositoryProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryProperty(java.lang.String)
     */
    @Override
    public void setRepositoryProperty(String repositoryProperty) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isValueSetToDefault()
     */
    @Override
    public boolean isValueSetToDefault() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRaw()
     */
    @Override
    public boolean isRaw() {
        return raw;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRaw(boolean)
     */
    @Override
    public void setRaw(boolean raw) {
        this.raw = raw;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isLog4JEnabled()
     */
    @Override
    public boolean isLog4JEnabled() {
        return this.enable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLog4JEnabled(boolean)
     */
    @Override
    public void setLog4JEnabled(boolean enable) {
        this.enable = enable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isSerialized()
     */
    @Override
    public boolean isSerialized() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setSerialized(boolean)
     */
    @Override
    public void setSerialized(boolean isSerialized) {
        // TODO Auto-generated method stub
    }
}
