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

    @Override
    public EComponentCategory getCategory() {
        return EComponentCategory.MAIN;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public String getExtension() {
        return null;
    }

    @Override
    public EParameterFieldType getFieldType() {
        return EParameterFieldType.TABLE;
    }

    @Override
    public String[] getListItemsValue() {
        return null;
    }

    @Override
    public String[] getListItemsDisplayName() {
        return null;
    }

    public String getMetadataType() {
        return "String"; //$NON-NLS-1$
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNbLines() {
        return 0;
    }

    @Override
    public int getNumRow() {
        return 0;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getVariableName() {
        return "__" + name + "__"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public void setCategory(EComponentCategory cat) {
        // Read-only
    }

    @Override
    public void setDisplayName(String s) {
        // Read-only
    }

    public void setExtension(String extension) {
        // Read-only
    }

    @Override
    public void setFieldType(EParameterFieldType type) {
        // Read-only
    }

    public void setListItemsValue(String[] list) {
        // Read-only
    }

    @Override
    public void setListItemsDisplayName(String[] list) {
        // Read-only
    }

    public void setMetadataType(String metadataType) {
        // Read-only
    }

    @Override
    public void setName(String s) {
        // Read-only
    }

    @Override
    public void setNbLines(int nbLines) {
        // Read-only
    }

    @Override
    public void setNumRow(int numRow) {
        // Read-only
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        // Read-only
    }

    @Override
    public void setRequired(boolean required) {
        // Read-only
    }

    @Override
    public void setShow(boolean show) {
        // Read-only
    }

    @Override
    public void setValue(Object o) {
        // Read-only
    }

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

    @Override
    public String getRepositoryValue() {
        return null;
    }

    @Override
    public void setRepositoryValue(String repositoryValue) {
    }

    @Override
    public boolean isRepositoryValueUsed() {
        return false;
    }

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

    @Override
    public void setShowIf(String showIf) {
    }

    @Override
    public String getShowIf() {
        return null;
    }

    @Override
    public void setNotShowIf(String notShowIf) {
    }

    @Override
    public String getNotShowIf() {
        return null;
    }

    @Override
    public boolean isShow(List<? extends IElementParameter> listParam) {
        return false;
    }

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

    @Override
    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam) {
        return false;
    }

    @Override
    public void setListItemsNotShowIf(String[] list) {
    }

    @Override
    public void setListItemsShowIf(String[] list) {
    }

    @Override
    public List<IElementParameterDefaultValue> getDefaultValues() {
        return null;
    }

    @Override
    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues) {
    }

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

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public void setGroup(String groupName) {

    }

    @Override
    public String getGroupDisplayName() {
        return null;
    }

    @Override
    public void setGroupDisplayName(String groupDisplayName) {

    }

    public Item getLinkedRepositoryItem() {
        return null;
    }

    public void setLinkedRepositoryItem(Item item) {

    }

    @Override
    public boolean isContextMode() {
        return this.contextMode;
    }

    @Override
    public void setContextMode(boolean contextMode) {
        this.contextMode = contextMode;
    }

    @Override
    public String getLabelFromRepository() {
        return null;
    }

    @Override
    public void setLabelFromRepository(String label) {

    }

    @Override
    public RGB getColor() {
        return this.color;
    }

    @Override
    public void setColor(RGB color) {
        this.color = color;
    }

    @Override
    public RGB getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    public void setBackgroundColor(RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public boolean isBasedOnSubjobStarts() {
        return false;
    }

    @Override
    public void setBasedOnSubjobStarts(boolean basedOnSubjobStarts) {
    }

    @Override
    public boolean isDynamicSettings() {
        return false;
    }

    @Override
    public void setDynamicSettings(boolean dynamicSettings) {

    }

    @Override
    public String[] getListItemsNotReadOnlyIf() {
        return null;
    }

    @Override
    public String[] getListItemsReadOnlyIf() {
        return null;
    }

    @Override
    public String getNotReadOnlyIf() {
        return null;
    }

    @Override
    public String getReadOnlyIf() {
        return null;
    }

    @Override
    public boolean isReadOnly(List<? extends IElementParameter> listParam) {
        return false;
    }

    @Override
    public boolean isReadOnly(String conditionReadOnlyIf, String conditionNotReadOnlyIf,
            List<? extends IElementParameter> listParams) {
        return false;
    }

    @Override
    public void setListItemsNotReadOnlyIf(String[] list) {

    }

    @Override
    public void setListItemsReadOnlyIf(String[] list) {

    }

    @Override
    public void setNotReadOnlyIf(String notShowIf) {

    }

    @Override
    public void setReadOnlyIf(String showIf) {

    }

    @Override
    public boolean isColumnsBasedOnSchema() {
        return false;
    }

    @Override
    public void setColumnsBasedOnSchema(boolean columnsBasedOnSchema) {

    }

    @Override
    public boolean isNoContextAssist() {
        return false;
    }

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

    @Override
    public int getMaxlength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isRequired(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
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
    public boolean isValueSetToDefault() {
        return false;
    }

    @Override
    public boolean isRaw() {
        return raw;
    }

    @Override
    public void setRaw(boolean raw) {
        this.raw = raw;
    }

    @Override
    public boolean isLog4JEnabled() {
        return this.enable;
    }

    @Override
    public void setLog4JEnabled(boolean enable) {
        this.enable = enable;
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
