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
package commons.ui.swt.formtools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;

/**
 * Test org.talend.commons.ui.swt.formtools<br/>
 * .
 * 
 * $Id: TestFormtools.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class TestFormtools {

    /**
     * Default Constructor. Must not be used.
     */
    private TestFormtools() {
    }

    private static final String WINDOWTITLE = "Test org.talend.commons.ui.swt.formtools"; //$NON-NLS-1$

    // private static Label statusLabel;

    private static Display display;

    @SuppressWarnings("unused")
    private static Button sampleButton;

    @SuppressWarnings("unused")
    private static Text sampleText;

    @SuppressWarnings("unused")
    private static Combo sampleCombo;

    /**
     * m�thode principale.
     * 
     * @param args
     */
    public static void main(String[] args) {

        // The Shell
        display = new Display();
        Shell shell = new Shell(display);
        shell.setText(WINDOWTITLE);
        shell.setLayout(new FillLayout());

        // Main Layout
        Composite composite = new Composite(shell, SWT.BORDER);
        composite.setLayout(new FillLayout());
        GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);

        // Utils : createStatusBar, startNewGridLayout, createGroup

        // use to SWTform / not use to Wizard

        Composite composite1 = Form.startNewGridLayout(composite, 1);

        Group group = Form.createGroup(composite1, 2, "Group Sample"); //$NON-NLS-1$
        Composite compositeGroup = Form.startNewGridLayout(group, 2);

        LabelledText nameText = new LabelledText(compositeGroup, "Name"); //$NON-NLS-1$
        nameText.setEditable(false);

        String[] itemTable = { "option1", "option2" }; //$NON-NLS-1$ //$NON-NLS-2$
        LabelledCombo tableCombo = new LabelledCombo(compositeGroup, "Table", "Aide � la saisie", itemTable); //$NON-NLS-1$ //$NON-NLS-2$

        String[] extensions = { "*.mdb" }; //$NON-NLS-1$
        LabelledFileField file = new LabelledFileField(compositeGroup, "Mdb File", extensions); //$NON-NLS-1$

        // Button Check
        Composite compositeCheckButton = Form.startNewGridLayout(composite1, 1, false, SWT.CENTER, SWT.CENTER);
        UtilsButton checkButton = new UtilsButton(compositeCheckButton, "C&heck", false); //$NON-NLS-1$

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
     * updateStatus.
     * 
     * @param String
     */
    // private static void updateStatus(String string) {
    // if (string != null) {
    // statusLabel.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    // statusLabel.setText(string);
    // } else {
    // statusLabel.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    // statusLabel.setText("");
    // }
    // }
}
