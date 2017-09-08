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
package org.talend.core.ui.context.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.runtime.utils.ControlUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.Problem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.i18n.Messages;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * ggu class global comment. Detailled comment
 */
public class ContextValueErrorChecker {

    private final FocusListener focusListenerForCheckingError = new FocusAdapter() {

        public void focusGained(FocusEvent event) {
            Control control = (Control) event.widget;
            checkErrorProblems(control);
        }

    };

    private final ModifyListener modifyListenerForCheckingError = new ModifyListener() {

        public void modifyText(ModifyEvent event) {
            Control control = (Control) event.widget;
            resetErrorState(control);
        }

    };

    private final TreeViewer viewer;

    private final Color oldForeColor;

    private final Color oldBackColor;

    private ICodeProblemsChecker syntaxChecker;

    private final ECodeLanguage language = LanguageManager.getCurrentLanguage();

    private final Color bgColorError;

    private final Color fgColorError;

    /**
     * 
     * ggu ContextValueErrorChecker constructor comment.
     */
    public ContextValueErrorChecker(final TreeViewer viewer) {
        super();
        this.viewer = viewer;

        this.oldForeColor = viewer.getTree().getForeground();
        this.oldBackColor = viewer.getTree().getBackground();
        this.bgColorError = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        this.fgColorError = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            this.syntaxChecker = runProcessService.getSyntaxChecker(language);

        }
    }

    public void register(final Control control) {
        Text textControl = getTextControl(control);
        if (textControl != null) {
            textControl.addFocusListener(focusListenerForCheckingError);
            textControl.addModifyListener(modifyListenerForCheckingError);
        }
    }

    public void unregister(final Control control) {
        Text textControl = getTextControl(control);
        if (textControl != null) {
            textControl.removeFocusListener(focusListenerForCheckingError);
            textControl.removeModifyListener(modifyListenerForCheckingError);
        }
    }

    private void checkErrorProblems(final Control control) {
        if (control == null || control.isDisposed()) {
            return;
        }
        if (!(control instanceof Text)) {
            return;
        }
        String value = ControlUtils.getText(control);
        List<Problem> problems = checkProblems(value);
        if (problems != null && problems.size() > 0) {

            control.setBackground(bgColorError);
            control.setForeground(fgColorError);
            String tooltip = Messages.getString("ContextValueErrorChecker.ErrorTitile"); //$NON-NLS-1$

            for (Problem problem : problems) {
                tooltip += "\n" + problem.getDescription(); //$NON-NLS-1$
            }
            control.setToolTipText(tooltip);
        } else {
            resetErrorState(control);
        }
    }

    private void resetErrorState(final Control control) {
        if (control == null || control.isDisposed()) {
            return;
        }
        Text textControl = getTextControl(control);
        if (textControl != null) {
            textControl.setToolTipText(""); //$NON-NLS-1$
            textControl.setBackground(oldBackColor);
            textControl.setForeground(oldForeColor);
        }

    }

    public Text getTextControl(final Control control) {
        if (control == null || control.isDisposed()) {
            return null;
        }
        if (control instanceof Text) {
            return (Text) control;
        }
        if (control instanceof Composite) {
            Control[] children = ((Composite) control).getChildren();
            if (children != null) {
                for (Control ctr : children) {
                    Text textControl = getTextControl(ctr);
                    if (textControl != null) {
                        return textControl;
                    }
                }
            }
        }
        return null;
    }

    public void checkErrors(final TreeItem item, final int column, IContextParameter param) {
        if (item == null || item.isDisposed() || column < 0) {
            return;
        }
        if (column >= viewer.getTree().getColumnCount()) {
            return;
        }
        String value = null;
        if (param == null) {
            value = item.getText(column);
        } else {
            value = param.getValue();
        }
        List<Problem> problems = checkProblems(value);

        if (problems != null && problems.size() > 0) {
            item.setBackground(column, bgColorError);
            item.setForeground(column, fgColorError);
        } else {
            resetErrorState(item);
        }
    }

    public void checkErrors(final TreeItem item, final int column) {
        checkErrors(item, column, null);
    }

    private void resetErrorState(final TreeItem item) {
        if (item == null || item.isDisposed()) {
            return;
        }
        item.setBackground(4, oldBackColor);
        item.setForeground(4, oldForeColor);
    }

    /**
     * 
     * check problems.
     */
    private List<Problem> checkProblems(String value) {
        if (value == null) {
            value = ""; //$NON-NLS-1$
        }
        List<Problem> problems = new ArrayList<Problem>();

        if (language == ECodeLanguage.PERL && syntaxChecker != null) {
            String expression = ContextParameterUtils.getNewScriptCode("new1", language); //$NON-NLS-1$
            expression += " = " + value + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            problems = syntaxChecker.checkProblemsForExpression(expression);
        } else if (language == ECodeLanguage.JAVA) {
            //
        }
        return problems;
    }
}
