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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class XPathTransfer extends ByteArrayTransfer {

    private XmlToSchemaDraggedData draggedData;

    private static final String XML_NODE_TO_XPATH_TYPE_NAME = "XML_NODE_TO_XPATH_ENTRIES"; //$NON-NLS-1$

    private static final int XML_NODE_TO_XPATH_ENTRIES_ID = registerType(XML_NODE_TO_XPATH_TYPE_NAME);

    private static final XPathTransfer INSTANCE = new XPathTransfer();

    public static XPathTransfer getInstance() {
        return INSTANCE;
    }

    @Override
    protected int[] getTypeIds() {
        return new int[] { XML_NODE_TO_XPATH_ENTRIES_ID };
    }

    @Override
    protected String[] getTypeNames() {
        return new String[] { XML_NODE_TO_XPATH_TYPE_NAME };
    }

    @Override
    protected void javaToNative(Object object, TransferData transferData) {
        // FIX for issue 1225
        super.javaToNative(new byte[1], transferData);
    }

    @Override
    protected Object nativeToJava(TransferData transferData) {
        return new byte[0];
    }

    public XmlToSchemaDraggedData getDraggedData() {
        return draggedData;
    }

    public void setDraggedData(XmlToSchemaDraggedData draggedData) {
        this.draggedData = draggedData;
    }

}
