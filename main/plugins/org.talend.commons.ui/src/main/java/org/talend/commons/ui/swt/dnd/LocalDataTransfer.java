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
package org.talend.commons.ui.swt.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TableEntriesTransfer.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class LocalDataTransfer extends ByteArrayTransfer {

    private LocalDraggedData draggedData;

    private static final String TYPE_NAME = "local-data-transfer-format" + (new Long(System.currentTimeMillis())).toString(); //$NON-NLS-1$;

    private static final int TYPE_ID = registerType(TYPE_NAME);

    private static final LocalDataTransfer INSTANCE = new LocalDataTransfer();

    public static LocalDataTransfer getInstance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    @Override
    protected int[] getTypeIds() {
        return new int[] { TYPE_ID };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     */
    @Override
    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
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

    public LocalDraggedData getDraggedData() {
        return draggedData;
    }

    public void setLocalDraggedData(LocalDraggedData draggedData) {
        this.draggedData = draggedData;
    }
}
