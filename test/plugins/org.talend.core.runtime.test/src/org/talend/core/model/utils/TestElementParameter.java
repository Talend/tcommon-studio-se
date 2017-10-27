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
package org.talend.core.model.utils;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;

/**
 * created by ggu on Sep 2, 2014 Detailled comment
 *
 */
class TestElementParameter implements IElementParameter {

    private Object value;

    private String repositoryValue;

    private EParameterFieldType fieldType;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setName(java.lang.String)
     */
    @Override
    public void setName(String s) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getVariableName()
     */
    @Override
    public String getVariableName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.process.IElementParameter#setCategory(org.talend.core.model.process.EComponentCategory)
     */
    @Override
    public void setCategory(EComponentCategory cat) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getCategory()
     */
    @Override
    public EComponentCategory getCategory() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDisplayName(java.lang.String)
     */
    @Override
    public void setDisplayName(String s) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.process.IElementParameter#setFieldType(org.talend.core.model.process.EParameterFieldType)
     */
    @Override
    public void setFieldType(EParameterFieldType type) {
        this.fieldType = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object o) {
        this.value = o;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getFieldType()
     */
    @Override
    public EParameterFieldType getFieldType() {
        return fieldType;
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
     * @see org.talend.core.model.process.IElementParameter#getNbLines()
     */
    @Override
    public int getNbLines() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNbLines(int)
     */
    @Override
    public void setNbLines(int nbLines) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNumRow()
     */
    @Override
    public int getNumRow() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNumRow(int)
     */
    @Override
    public void setNumRow(int numRow) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRequired()
     */
    @Override
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return false;
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
     * @see org.talend.core.model.process.IElementParameter#setRequired(boolean)
     */
    @Override
    public void setRequired(boolean required) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setShow(boolean)
     */
    @Override
    public void setShow(boolean show) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDefaultClosedListValue(java.lang.Object)
     */
    @Override
    public void setDefaultClosedListValue(Object o) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsDisplayName(java.lang.String[])
     */
    @Override
    public void setListItemsDisplayName(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsDisplayCodeName(java.lang.String[])
     */
    @Override
    public void setListItemsDisplayCodeName(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsValue(java.lang.Object[])
     */
    @Override
    public void setListItemsValue(Object[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDefaultClosedListValue()
     */
    @Override
    public Object getDefaultClosedListValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsDisplayName()
     */
    @Override
    public String[] getListItemsDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsDisplayCodeName()
     */
    @Override
    public String[] getListItemsDisplayCodeName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsValue()
     */
    @Override
    public Object[] getListItemsValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getRepositoryValue()
     */
    @Override
    public String getRepositoryValue() {
        return repositoryValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryValue(java.lang.String)
     */
    @Override
    public void setRepositoryValue(String repositoryValue) {
        this.repositoryValue = repositoryValue;
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
     * @see org.talend.core.model.process.IElementParameter#isRepositoryValueUsed()
     */
    @Override
    public boolean isRepositoryValueUsed() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryValueUsed(boolean)
     */
    @Override
    public void setRepositoryValueUsed(boolean repositoryUsed) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListRepositoryItems()
     */
    @Override
    public String[] getListRepositoryItems() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListRepositoryItems(java.lang.String[])
     */
    @Override
    public void setListRepositoryItems(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getIndexOfItemFromList(java.lang.String)
     */
    @Override
    public int getIndexOfItemFromList(String item) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getShowIf()
     */
    @Override
    public String getShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setShowIf(java.lang.String)
     */
    @Override
    public void setShowIf(String showIf) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNotShowIf()
     */
    @Override
    public String getNotShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNotShowIf(java.lang.String)
     */
    @Override
    public void setNotShowIf(String notShowIf) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isShow(java.util.List)
     */
    @Override
    public boolean isShow(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsShowIf(java.lang.String[])
     */
    @Override
    public void setListItemsShowIf(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsShowIf()
     */
    @Override
    public String[] getListItemsShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotShowIf(java.lang.String[])
     */
    @Override
    public void setListItemsNotShowIf(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsNotShowIf()
     */
    @Override
    public String[] getListItemsNotShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isShow(java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getReadOnlyIf()
     */
    @Override
    public String getReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setReadOnlyIf(java.lang.String)
     */
    @Override
    public void setReadOnlyIf(String showIf) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNotReadOnlyIf()
     */
    @Override
    public String getNotReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNotReadOnlyIf(java.lang.String)
     */
    @Override
    public void setNotReadOnlyIf(String notShowIf) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly(java.util.List)
     */
    @Override
    public boolean isReadOnly(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsReadOnlyIf(java.lang.String[])
     */
    @Override
    public void setListItemsReadOnlyIf(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsReadOnlyIf()
     */
    @Override
    public String[] getListItemsReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotReadOnlyIf(java.lang.String[])
     */
    @Override
    public void setListItemsNotReadOnlyIf(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsNotReadOnlyIf()
     */
    @Override
    public String[] getListItemsNotReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDefaultValues()
     */
    @Override
    public List<IElementParameterDefaultValue> getDefaultValues() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDefaultValues(java.util.List)
     */
    @Override
    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValueToDefault(java.util.List)
     */
    @Override
    public void setValueToDefault(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setElement(org.talend.core.model.process.IElement)
     */
    @Override
    public void setElement(IElement element) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getElement()
     */
    @Override
    public IElement getElement() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isBasedOnSchema()
     */
    @Override
    public boolean isBasedOnSchema() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setBasedOnSchema(boolean)
     */
    @Override
    public void setBasedOnSchema(boolean basedOnSchema) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isBasedOnSubjobStarts()
     */
    @Override
    public boolean isBasedOnSubjobStarts() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isColumnsBasedOnSchema()
     */
    @Override
    public boolean isColumnsBasedOnSchema() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setColumnsBasedOnSchema(boolean)
     */
    @Override
    public void setColumnsBasedOnSchema(boolean columnsBasedOnSchema) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setBasedOnSubjobStarts(boolean)
     */
    @Override
    public void setBasedOnSubjobStarts(boolean basedOnSubjobStarts) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getFilter()
     */
    @Override
    public String getFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setFilter(java.lang.String)
     */
    @Override
    public void setFilter(String filter) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isNoCheck()
     */
    @Override
    public boolean isNoCheck() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNoCheck(boolean)
     */
    @Override
    public void setNoCheck(boolean noCheck) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getContext()
     */
    @Override
    public String getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setContext(java.lang.String)
     */
    @Override
    public void setContext(String context) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getGroup()
     */
    @Override
    public String getGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setGroup(java.lang.String)
     */
    @Override
    public void setGroup(String groupName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setGroupDisplayName(java.lang.String)
     */
    @Override
    public void setGroupDisplayName(String groupDisplayName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getGroupDisplayName()
     */
    @Override
    public String getGroupDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getChildParameters()
     */
    @Override
    public Map<String, IElementParameter> getChildParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getParentParameter()
     */
    @Override
    public IElementParameter getParentParameter() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.process.IElementParameter#setParentParameter(org.talend.core.model.process.IElementParameter
     * )
     */
    @Override
    public void setParentParameter(IElementParameter parentParameter) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isContextMode()
     */
    @Override
    public boolean isContextMode() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setContextMode(boolean)
     */
    @Override
    public void setContextMode(boolean mode) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLabelFromRepository(java.lang.String)
     */
    @Override
    public void setLabelFromRepository(String label) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getLabelFromRepository()
     */
    @Override
    public String getLabelFromRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setBackgroundColor(org.eclipse.swt.graphics.RGB)
     */
    @Override
    public void setBackgroundColor(RGB bgColor) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getBackgroundColor()
     */
    @Override
    public RGB getBackgroundColor() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setColor(org.eclipse.swt.graphics.RGB)
     */
    @Override
    public void setColor(RGB color) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getColor()
     */
    @Override
    public RGB getColor() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isDynamicSettings()
     */
    @Override
    public boolean isDynamicSettings() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDynamicSettings(boolean)
     */
    @Override
    public void setDynamicSettings(boolean dynamicSettings) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isNoContextAssist()
     */
    @Override
    public boolean isNoContextAssist() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNoContextAssist(boolean)
     */
    @Override
    public void setNoContextAssist(boolean enable) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getClone()
     */
    @Override
    public IElementParameter getClone() {
        // TODO Auto-generated method stub
        return null;
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
     * @see org.talend.core.model.process.IElementParameter#isValueSetToDefault()
     */
    @Override
    public boolean isValueSetToDefault() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRaw()
     */
    @Override
    public boolean isRaw() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRaw(boolean)
     */
    @Override
    public void setRaw(boolean raw) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLog4JEnabled(boolean)
     */
    @Override
    public void setLog4JEnabled(boolean enable) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isLog4JEnabled()
     */
    @Override
    public boolean isLog4JEnabled() {
        // TODO Auto-generated method stub
        return false;
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
