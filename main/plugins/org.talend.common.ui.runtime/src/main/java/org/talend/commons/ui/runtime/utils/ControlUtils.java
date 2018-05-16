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
package org.talend.commons.ui.runtime.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: ControlUtils.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ControlUtils {

    /**
     * 
     * DOC amaumont Comment method "getText".
     * 
     * @param control
     * @return
     */
    public static String getText(Control control) {
        // use reflection to copy with any StyledText that is not compatible with RAP
        Object result = invokeMethod(control, "getText"); //$NON-NLS-1$
        if (result instanceof String) {
            return (String) result;
        } else {
            throw new UnsupportedOperationException(Messages.getString("ControlUtils.Unsupported1", control.getClass())); //$NON-NLS-1$
        }
    }

    /**
     * DOC sgandon Comment method "invokeMethod".
     * 
     * @param control
     * @param value
     * @return
     */
    private static Object invokeMethod(Control control, String methodName) {
        try {
            Method getTextMethod = control.getClass().getMethod(methodName, (Class<?>[]) null);
            return getTextMethod.invoke(control);
        } catch (SecurityException e) {// All these exception should never happen but we surface them in case
            throw new InvocationFailedException(e);
        } catch (NoSuchMethodException e) {
            throw new InvocationFailedException(e);
        } catch (IllegalArgumentException e) {
            throw new InvocationFailedException(e);
        } catch (IllegalAccessException e) {
            throw new InvocationFailedException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationFailedException(e);
        }
    }

    /**
     * DOC sgandon Comment method "invokeMethod".
     * 
     * @param control
     * @param value
     * @return
     */
    private static Object invokeOneParamMethod(Control control, String methodName, Object param, Class clazz) {
        try {
            if (clazz == null) {
                clazz = param.getClass();
            }
            Method getMethod = control.getClass().getMethod(methodName, clazz);
            return getMethod.invoke(control, param);
        } catch (SecurityException e) {// All these exception should never happen but we surface them in case
            throw new InvocationFailedException(e);
        } catch (NoSuchMethodException e) {
            throw new InvocationFailedException(e);
        } catch (IllegalArgumentException e) {
            throw new InvocationFailedException(e);
        } catch (IllegalAccessException e) {
            throw new InvocationFailedException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationFailedException(e);
        }
    }

    /**
     * 
     * DOC amaumont Comment method "getText".
     * 
     * @param control
     * @return
     */
    public static void setText(Control control, String text) {
        invokeOneParamMethod(control, "setText", text, null); //$NON-NLS-1$
    }

    /**
     * 
     * DOC amaumont Comment method "getText".
     * 
     * @param control
     * @return
     */
    public static Point getSelection(Control control) {
        // use reflection to copy with any StyledText that is not compatible with RAP
        Object result = invokeMethod(control, "getSelection"); //$NON-NLS-1$
        if (result instanceof Point) {
            return (Point) result;
        } else {
            throw new UnsupportedOperationException(Messages.getString("ControlUtils.Unsupported1", control.getClass())); //$NON-NLS-1$
        }
    }

    /**
     * 
     * DOC amaumont Comment method "getText".
     * 
     * @param control
     * @return
     */
    public static String getLineDelimiter(Control control) {
        // use reflection to copy with any StyledText that is not compatible with RAP
        Object result = invokeMethod(control, "getLineDelimiter"); //$NON-NLS-1$
        if (result instanceof String) {
            return (String) result;
        } else {
            throw new UnsupportedOperationException(Messages.getString("ControlUtils.Unsupported1", control.getClass())); //$NON-NLS-1$
        }
    }

    /**
     * 
     * DOC amaumont Comment method "getText".
     * 
     * @param control
     * @return
     */
    public static void addModifyListener(Control control, ModifyListener modifyListener) {
        invokeOneParamMethod(control, "addModifyListener", modifyListener, null); //$NON-NLS-1$
    }

    /**
     * DOC amaumont Comment method "removeModifyListener".
     * 
     * @param control
     * @param modifyListener
     */
    public static void removeModifyListener(Control control, ModifyListener modifyListener) {
        invokeOneParamMethod(control, "removeModifyListener", modifyListener, null); //$NON-NLS-1$
    }

    public static int getCursorPosition(Control control) {
        int position = -1;
        // use reflection to copy with any StyledText that is not compatible with RAP
        try {
            Object result = invokeMethod(control, "getCaretPosition"); //$NON-NLS-1$
            if (result instanceof Integer) {
                position = (Integer) result;
            } else {
                throw new UnsupportedOperationException(Messages.getString("ControlUtils.Unsupported1", control.getClass())); //$NON-NLS-1$
            }
        } catch (InvocationFailedException e) {
            // try with StyledText.getCaretOffset
            Object result = invokeMethod(control, "getCaretOffset"); //$NON-NLS-1$
            if (result instanceof Integer) {
                position = (Integer) result;
            } else {
                throw new UnsupportedOperationException(Messages.getString("ControlUtils.Unsupported1", control.getClass())); //$NON-NLS-1$
            }
        }
        return position;
    }

    public static void setCursorPosition(Control control, int position) {
        invokeOneParamMethod(control, "setSelection", position, int.class); //$NON-NLS-1$
    }

    public static void setSortedValuesForCombo(CCombo combo, String[] values) {
        Arrays.sort(values);
        combo.setItems(values);
    }

    static class InvocationFailedException extends RuntimeException {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * DOC sgandon InvocationFailed constructor comment.
         * 
         * @param e
         */
        public InvocationFailedException(Throwable e) {
            super(e);
        }
    };
}
