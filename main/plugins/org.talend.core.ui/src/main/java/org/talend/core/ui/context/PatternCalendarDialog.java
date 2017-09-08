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
package org.talend.core.ui.context;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.DateDialog;
import org.talend.core.model.process.IContextParameter;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class PatternCalendarDialog extends DateDialog {

    private IContextParameter param;

    private PatternCalendar time;

    /**
     * DOC chuang PatternCalendarDialog constructor comment.
     * 
     * @param parentShell
     * @param param
     */
    protected PatternCalendarDialog(Shell parentShell, IContextParameter param) {
        super(parentShell);
        setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        this.param = param;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite control2 = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginLeft = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        control2.setLayout(layout);
        control2.setLayoutData(new GridData(GridData.FILL_BOTH));

        String value = param.getValue();
        String pattern = null;
        Calendar calendar = null;

        if (!StringUtils.isEmpty(value)) {
            int pos = value.indexOf(";"); //$NON-NLS-1$
            if (pos > -1) {
                pattern = value.substring(0, pos);
                String date = value.substring(pos + 1);
                calendar = getCalendar(pattern, date);
            } else {
                calendar = getCalendar("yyyy-MM-dd HH:mm:ss", value); //$NON-NLS-1$
            }
        }
        time = new PatternCalendar(control2, SWT.NONE, pattern, calendar);
        return control2;
    }

    private Calendar getCalendar(String pattern, String value) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            calendar.setTime(sdf.parse(value));
        } catch (ParseException e) {
            ExceptionHandler.process(e);
            return null;
        }
        return calendar;
    }

    @Override
    public Date getDate() {
        return time.getCalendar().getTime();
    }

    @Override
    public String getTalendDateString() {
        StringBuffer result = new StringBuffer();

        String pattern = time.getPatternText();
        if (pattern == null || pattern == "") { //$NON-NLS-1$
            pattern = "yyyy-MM-dd HH:mm:ss"; //$NON-NLS-1$
        } else if (pattern.startsWith("\"")) { //$NON-NLS-1$
            // remove quotes
            pattern = pattern.substring(1, pattern.length() - 1);

        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.format(getDate(), result, new FieldPosition(0));
        return pattern + ";" + result.toString(); //$NON-NLS-1$
    }

}
