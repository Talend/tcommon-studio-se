// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

class TestElementParameter implements IElementParameter {

    private Object value;

    private String repositoryValue;

    private EParameterFieldType fieldType;

    @Override
    public void setName(String s) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getVariableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCategory(EComponentCategory cat) {
        // TODO Auto-generated method stub

    }

    @Override
    public EComponentCategory getCategory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDisplayName(String s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFieldType(EParameterFieldType type) {
        this.fieldType = type;
    }

    @Override
    public void setValue(Object o) {
        this.value = o;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EParameterFieldType getFieldType() {
        return fieldType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int getNbLines() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setNbLines(int nbLines) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNumRow() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setNumRow(int numRow) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequired(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setRequired(boolean required) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setShow(boolean show) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDefaultClosedListValue(Object o) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListItemsDisplayName(String[] list) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListItemsDisplayCodeName(String[] list) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListItemsValue(Object[] list) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getDefaultClosedListValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getListItemsDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getListItemsDisplayCodeName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] getListItemsValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRepositoryValue() {
        return repositoryValue;
    }

    @Override
    public void setRepositoryValue(String repositoryValue) {
        this.repositoryValue = repositoryValue;
    }

    @Override
    public String getRepositoryProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRepositoryProperty(String repositoryProperty) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRepositoryValueUsed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setRepositoryValueUsed(boolean repositoryUsed) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getListRepositoryItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setListRepositoryItems(String[] list) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getIndexOfItemFromList(String item) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShowIf(String showIf) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getNotShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setNotShowIf(String notShowIf) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isShow(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setListItemsShowIf(String[] list) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getListItemsShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setListItemsNotShowIf(String[] list) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getListItemsNotShowIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setReadOnlyIf(String showIf) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getNotReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setNotReadOnlyIf(String notShowIf) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isReadOnly(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setListItemsReadOnlyIf(String[] list) {
        // TODO Auto-generated method stub
    }

    @Override
    public String[] getListItemsReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setListItemsNotReadOnlyIf(String[] list) {
        // TODO Auto-generated method stub
    }

    @Override
    public String[] getListItemsNotReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isReadOnly(String conditionReadOnlyIf, String conditionNotReadOnlyIf,
            List<? extends IElementParameter> listParams) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<IElementParameterDefaultValue> getDefaultValues() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setValueToDefault(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setElement(IElement element) {
        // TODO Auto-generated method stub
    }

    @Override
    public IElement getElement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isBasedOnSchema() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setBasedOnSchema(boolean basedOnSchema) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isBasedOnSubjobStarts() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isColumnsBasedOnSchema() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setColumnsBasedOnSchema(boolean columnsBasedOnSchema) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setBasedOnSubjobStarts(boolean basedOnSubjobStarts) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFilter(String filter) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isNoCheck() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setNoCheck(boolean noCheck) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setContext(String context) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGroup(String groupName) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setGroupDisplayName(String groupDisplayName) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getGroupDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, IElementParameter> getChildParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IElementParameter getParentParameter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParentParameter(IElementParameter parentParameter) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isContextMode() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setContextMode(boolean mode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setLabelFromRepository(String label) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getLabelFromRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBackgroundColor(RGB bgColor) {
        // TODO Auto-generated method stub
    }

    @Override
    public RGB getBackgroundColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setColor(RGB color) {
        // TODO Auto-generated method stub
    }

    @Override
    public RGB getColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isDynamicSettings() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setDynamicSettings(boolean dynamicSettings) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isNoContextAssist() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setNoContextAssist(boolean enable) {
        // TODO Auto-generated method stub
    }

    @Override
    public IElementParameter getClone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxlength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isValueSetToDefault() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRaw() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setRaw(boolean raw) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setLog4JEnabled(boolean enable) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isLog4JEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSerialized() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSerialized(boolean isSerialized) {
        // TODO Auto-generated method stub
    }
}
