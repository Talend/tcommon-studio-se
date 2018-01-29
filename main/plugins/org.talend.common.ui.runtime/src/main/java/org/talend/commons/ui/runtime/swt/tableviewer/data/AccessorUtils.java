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
package org.talend.commons.ui.runtime.swt.tableviewer.data;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: AccessorUtils.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class AccessorUtils {

    /**
     * Default Constructor. Must not be used.
     */
    private AccessorUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Object get(Object bean, TableViewerCreatorColumnNotModifiable column) {
        Object value = null;
        if (column.getBeanPropertyAccessors() != null) {
            try {
                value = column.getBeanPropertyAccessors().get(bean);
            } catch (ClassCastException cce) {

                String message = Messages.getString("AccessorUtils.Assert0", column.getTitle(), column.getId(), bean.getClass()); //$NON-NLS-1$
                // cce.printStackTrace();
                ExceptionHandler.process(cce);
                throw new RuntimeException(message, cce);
            } catch (NoClassDefFoundError e) {
                // e.printStackTrace();
                ExceptionHandler.process(e);
                System.err.println(Messages.getString("AccessorUtils.NoClassDef", AccessorUtils.class, e.getMessage())); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static void set(TableViewerCreatorColumnNotModifiable column, Object bean, Object value) {
        if (column.getBeanPropertyAccessors() != null) {
            try {
                column.getBeanPropertyAccessors().set(bean, value);
            } catch (ClassCastException cce) {

                String message = Messages.getString("AccessorUtils.Assert1", column.getTitle(), column.getId()); //$NON-NLS-1$
                if (bean != null) {
                    message += "\n " + bean.getClass() + Messages.getString("AccessorUtils.isReq"); //$NON-NLS-1$//$NON-NLS-2$
                }
                if (value != null) {
                    message += "\n " + value.getClass() + Messages.getString("AccessorUtils.isReqValue"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                throw new RuntimeException(message, cce);
            }
        }
    }

}
