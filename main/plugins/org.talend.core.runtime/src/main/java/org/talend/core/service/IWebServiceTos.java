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
package org.talend.core.service;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.core.IService;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.ui.AbstractWebService;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public interface IWebServiceTos extends IService {

    public AbstractWebService getWebServiceUI(Composite uiParent, ConnectionItem connectionItem);

    public AbstractWebService getWebServiceUI(Composite uiParent, ConnectionItem connectionItem, INode node);

    public Boolean getCurrentFunction();

    public Table getTable();

    public void saveValue();

    public LabelledFileField getWSDLLabel(Boolean b);

    public void refreshUI(String url);

}
