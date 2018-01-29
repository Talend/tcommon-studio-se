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
package org.talend.commons.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.i18n.Messages;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class StateComposite extends Composite {

    private Label num1;

    private StyledText step1;

    private Label num2;

    private StyledText step2;

    private Label num3;

    private StyledText step3;

    private Label num4;

    private StyledText step4;

    private Label num5;

    private StyledText step5;

    private boolean istarget;

    private boolean isJoblet;

    /**
     * DOC hwang StateComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public StateComposite(Composite parent, boolean isJoblet, int style) {
        super(parent, style);
        this.isJoblet = isJoblet;
        createControls();
    }

    public StateComposite(Composite parent, int style, boolean istarget, boolean isJoblet) {
        super(parent, style);
        this.istarget = istarget;
        this.isJoblet = isJoblet;
        createControls();
    }

    private void createControls() {
        setLayout(new GridLayout());
        Group checkName = new Group(this, SWT.SHADOW_NONE);
        checkName.setText(Messages.getString("StateComposite.STATE")); //$NON-NLS-1$
        checkName.setLayoutData(new GridData(GridData.FILL_BOTH));
        checkName.setLayout(new GridLayout(5, false));
        GridData data1 = new GridData();
        data1.horizontalSpan = 1;
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.horizontalSpan = 5;
        Font font = new Font(Display.getDefault(), "", 9, SWT.NORMAL); //$NON-NLS-1$
        int i = 1;
        num1 = new Label(checkName, SWT.NONE);
        num1.setText(i + ".");//$NON-NLS-1$
        i++;
        num1.setLayoutData(data1);
        step1 = new StyledText(checkName, SWT.NONE);
        step1.setEnabled(false);
        step1.setText(Messages.getString("StateComposite.TEMPLATE_SELECTION")); //$NON-NLS-1$
        step1.setLayoutData(data);
        step1.setBackground(new Color(Display.getDefault(), new RGB(238, 234, 221)));
        step1.setFont(font);

        if (!isJoblet) {
            num2 = new Label(checkName, SWT.NONE);
            num2.setText(i + ".");//$NON-NLS-1$
            i++;
            num2.setLayoutData(data1);
            step2 = new StyledText(checkName, SWT.NONE);
            step2.setEnabled(false);
            step2.setText(Messages.getString("StateComposite.FLL_DB_INFOR")); //$NON-NLS-1$
            step2.setLayoutData(data);
            step2.setBackground(new Color(Display.getDefault(), new RGB(238, 234, 221)));
            step2.setFont(font);
        }

        num3 = new Label(checkName, SWT.NONE);
        num3.setText(i + ".");//$NON-NLS-1$
        i++;
        num3.setLayoutData(data1);
        step3 = new StyledText(checkName, SWT.NONE);
        step3.setEnabled(false);
        step3.setText(Messages.getString("StateComposite.TABLE_SELECTION")); //$NON-NLS-1$
        step3.setLayoutData(data);
        step3.setBackground(new Color(Display.getDefault(), new RGB(238, 234, 221)));
        step3.setFont(font);

        num4 = new Label(checkName, SWT.NONE);
        num4.setText(i + ".");//$NON-NLS-1$
        i++;
        num4.setLayoutData(data1);
        step4 = new StyledText(checkName, SWT.NONE);
        step4.setEnabled(false);
        step4.setText(Messages.getString("StateComposite.OUTPUT_SELECTION")); //$NON-NLS-1$
        step4.setLayoutData(data);
        step4.setBackground(new Color(Display.getDefault(), new RGB(238, 234, 221)));
        step4.setFont(font);

        num5 = new Label(checkName, SWT.NONE);
        num5.setText(i + ".");//$NON-NLS-1$
        i++;
        num5.setLayoutData(data1);
        step5 = new StyledText(checkName, SWT.NONE);
        step5.setEnabled(false);
        step5.setText(Messages.getString("StateComposite.SEL_JOBNAME")); //$NON-NLS-1$
        step5.setLayoutData(data);
        step5.setBackground(new Color(Display.getDefault(), new RGB(238, 234, 221)));
        step5.setFont(font);
        if (istarget) {
            createHelpMessage(this);
        }
    }

    private void createHelpMessage(Composite typeGroup) {
        Group messageGroup = new Group(typeGroup, SWT.NONE);// SWT.SHADOW_NONE);
        messageGroup.setText(Messages.getString("StateComposite.HELP_MESSAGE")); //$NON-NLS-1$
        GridData data = new GridData(GridData.FILL_BOTH);
        messageGroup.setLayoutData(data);
        messageGroup.setLayout(new GridLayout(1, false));
        messageGroup.setSize(30, 30);

        StyledText messageLabel = new StyledText(messageGroup, SWT.WRAP);
        messageLabel.setEnabled(false);
        messageLabel.setBackground(new Color(Display.getDefault(), new RGB(238, 234, 221)));
        messageLabel.setForeground(new Color(Display.getDefault(), new RGB(0, 102, 51)));
        messageLabel.pack();
        String newLine = "\r\n";//$NON-NLS-1$
        String currTable = "{CURRENT_TABLE}";//$NON-NLS-1$
        String path = "\"C:/myDirectory/{CURRENT_TABLE}.csv\"\r\n";//$NON-NLS-1$
        String mess1 = Messages.getString("StateComposite.MESSAGE1"); //$NON-NLS-1$
        String mess2 = Messages.getString("StateComposite.MESSAGE2"); //$NON-NLS-1$
        String mess3 = Messages.getString("StateComposite.MESSAGE3"); //$NON-NLS-1$
        String mess4 = Messages.getString("StateComposite.MESSAGE4"); //$NON-NLS-1$
        String mess8 = Messages.getString("StateComposite.MESSAGE5"); //$NON-NLS-1$
        String mess5 = Messages.getString("StateComposite.MESSAGE6"); //$NON-NLS-1$
        String mess6 = "\"{CURRENT_TABLE}\"";//$NON-NLS-1$
        String mess7 = "\"DWH_{CURRENT_TABLE}\"";//$NON-NLS-1$
        StringBuffer helpMessage = new StringBuffer();// = mess1+newLine;
        helpMessage.append(mess1);
        helpMessage.append(newLine);
        helpMessage.append(mess2);
        helpMessage.append(currTable);
        helpMessage.append(mess3);
        helpMessage.append(newLine);
        helpMessage.append(mess8);
        helpMessage.append(path);
        helpMessage.append(mess4);
        helpMessage.append(mess6);
        helpMessage.append(mess5);
        helpMessage.append(mess7);
        // messageLabel
        // .setText(
        // "It's possible to create dynamic jobs and set a file name or table name who depends on the input table.\r\n"
        // + "For this it just needs to use the text {CURRENT_TABLE} in the file name or table name.\r\n"
        // + "For example for one file: \"C:/myDirectory/{CURRENT_TABLE}.csv\"\r\n"
        // + "For example for one table: \"{CURRENT_TABLE}\" or maybe \"DWH_{CURRENT_TABLE}\"");
        messageLabel.setText(helpMessage.toString());
        data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 200;
        data.minimumWidth = 200;
        messageLabel.setLayoutData(data);
    }

    public void refreshState(String state) {
        if (state.equals("step1")) {//$NON-NLS-1$
            step1.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = step1.getCharCount();// 17
            style.underline = true;
            step1.setStyleRange(style);
        } else if (step2 != null && state.equals("step2")) {//$NON-NLS-1$
            step2.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = step2.getCharCount();// 19
            style.underline = true;
            step2.setStyleRange(style);
        } else if (state.equals("step3")) {//$NON-NLS-1$
            step3.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = step3.getCharCount();// 15;
            style.underline = true;
            step3.setStyleRange(style);
        } else if (state.equals("step4")) {//$NON-NLS-1$
            step4.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = step4.getCharCount();// 16;
            style.underline = true;
            step4.setStyleRange(style);
        } else if (state.equals("step5")) {//$NON-NLS-1$
            step5.setForeground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = step5.getCharCount();// 13;
            style.underline = true;
            step5.setStyleRange(style);
        }
    }
}
