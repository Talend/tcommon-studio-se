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
package org.talend.core.model.process;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IElementParameter.java 39167 2010-03-26 06:55:10Z cli $
 * 
 */
public interface IElementParameter {

    public void setName(final String s);

    public String getVariableName();

    public void setCategory(final EComponentCategory cat);

    public EComponentCategory getCategory();

    public void setDisplayName(final String s);

    public void setFieldType(final EParameterFieldType type);

    public void setValue(final Object o);

    public String getName();

    public String getDisplayName();

    public EParameterFieldType getFieldType();

    public Object getValue();

    public int getNbLines();

    public void setNbLines(final int nbLines);

    public int getNumRow();

    public void setNumRow(final int numRow);

    public boolean isReadOnly();

    public void setReadOnly(final boolean readOnly);

    public boolean isRequired();

    public boolean isRequired(List<? extends IElementParameter> listParam);

    public void setRequired(final boolean required);

    public void setShow(final boolean show);

    // for combo box (CLOSED_LIST)
    public void setDefaultClosedListValue(final Object o);

    public void setListItemsDisplayName(final String[] list);

    public void setListItemsDisplayCodeName(final String[] list);

    public void setListItemsValue(final Object[] list);

    public Object getDefaultClosedListValue();

    public String[] getListItemsDisplayName();

    public String[] getListItemsDisplayCodeName();

    public Object[] getListItemsValue();

    public String getRepositoryValue();

    public void setRepositoryValue(String repositoryValue);

    public String getRepositoryProperty();

    public void setRepositoryProperty(String repositoryProperty);

    public boolean isRepositoryValueUsed();

    public void setRepositoryValueUsed(boolean repositoryUsed);

    public String[] getListRepositoryItems();

    public void setListRepositoryItems(final String[] list);

    public int getIndexOfItemFromList(String item);

    public String getShowIf();

    public void setShowIf(String showIf);

    public String getNotShowIf();

    public void setNotShowIf(String notShowIf);

    public boolean isShow(List<? extends IElementParameter> listParam);

    public void setListItemsShowIf(final String[] list);

    public String[] getListItemsShowIf();

    public void setListItemsNotShowIf(final String[] list);

    public String[] getListItemsNotShowIf();

    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam);

    public String getReadOnlyIf();

    public void setReadOnlyIf(String showIf);

    public String getNotReadOnlyIf();

    public void setNotReadOnlyIf(String notShowIf);

    public boolean isReadOnly(List<? extends IElementParameter> listParam);

    public void setListItemsReadOnlyIf(final String[] list);

    public String[] getListItemsReadOnlyIf();

    public void setListItemsNotReadOnlyIf(final String[] list);

    public String[] getListItemsNotReadOnlyIf();

    public boolean isReadOnly(String conditionReadOnlyIf, String conditionNotReadOnlyIf,
            List<? extends IElementParameter> listParams);

    public List<IElementParameterDefaultValue> getDefaultValues();

    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues);

    public void setValueToDefault(List<? extends IElementParameter> listParam);

    public void setElement(IElement element);

    public IElement getElement();

    public boolean isBasedOnSchema();

    public void setBasedOnSchema(boolean basedOnSchema);

    public boolean isBasedOnSubjobStarts();

    public boolean isColumnsBasedOnSchema();

    public void setColumnsBasedOnSchema(boolean columnsBasedOnSchema);

    public void setBasedOnSubjobStarts(boolean basedOnSubjobStarts);

    public String getFilter();

    public void setFilter(String filter);

    public boolean isNoCheck();

    public void setNoCheck(boolean noCheck);

    public String getContext();

    public void setContext(String context);

    public String getGroup();

    public void setGroup(String groupName);

    public void setGroupDisplayName(String groupDisplayName);

    public String getGroupDisplayName();

    public Map<String, IElementParameter> getChildParameters();

    public IElementParameter getParentParameter();

    public void setParentParameter(IElementParameter parentParameter);

    public boolean isContextMode();

    public void setContextMode(boolean mode);

    public void setLabelFromRepository(String label);

    public String getLabelFromRepository();

    public void setBackgroundColor(RGB bgColor);

    public RGB getBackgroundColor();

    public void setColor(RGB color);

    public RGB getColor();

    public boolean isDynamicSettings();

    public void setDynamicSettings(boolean dynamicSettings);

    public boolean isNoContextAssist();

    public void setNoContextAssist(boolean enable);

    public IElementParameter getClone();

    public int getMaxlength();

    public boolean isValueSetToDefault();

    public boolean isRaw();

    public void setRaw(boolean raw);

    public void setLog4JEnabled(boolean enable);

    public boolean isLog4JEnabled();

    public boolean isSerialized();

    public void setSerialized(boolean isSerialized);
}
