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
package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Test
 * 
 * $Id: Test4.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */

public class Test4 {

    static PositionalTextTest positionalText;

    static RegleTest regleTest;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Test"); //$NON-NLS-1$

        regleTest = new RegleTest(shell, SWT.NONE);
        regleTest.setBounds(3, 0, 500, 25);

        positionalText = new PositionalTextTest(shell, SWT.NONE);
        positionalText.setBounds(0, 25, 500, 500);
        positionalText.setText("\n123456789\niin texte\nsalut le test\ntoto"); //$NON-NLS-1$
        // positionalText.setFieldSeparatorValue("1,1");
        regleTest.moveAbove(positionalText);

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
