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
package org.talend.commons.ui.swt.formtools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Utilities class uses to create swt forms. <br/>
 * swt Form add a statusLabel who is a default element of wizard
 * 
 * $Id: Form.java 7038 2007-11-15 14:05:48Z plegall $
 */
public class Form {

    /**
     * create StatusBar.
     * 
     * @param Composite parent
     * @param int color
     * 
     * @return Group
     */
    // public static Label createStatusBar(Composite parent, Color color) {
    // Label label = new Label(parent, SWT.LEFT);
    // label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    // label.setForeground(color);
    // return label;
    // }
    /**
     * create Group.
     * 
     * @param Composite parent
     * @param int columns
     * 
     * @return Group
     */
    public Group createGroup(Composite parent, int columns) {
        return createGroup(parent, columns, null);
    }

    /**
     * create Group.
     * 
     * @param Composite parent
     * @param int columns
     * @param String title
     * 
     * @return Group
     */
    public static Group createGroup(Composite parent, int columns, String title) {
        return createGroup(parent, columns, title, 0);
    }

    /**
     * create Group.
     * 
     * @param Composite parent
     * @param int columns
     * @param String title
     * 
     * @return Group
     */
    public static Group createGroup(Composite parent, int columns, String title, int minimumHeight) {
        Group group = new Group(parent, SWT.NONE);
        if (title != null) {
            group.setText(title);
        }
        // use X columns, not same width
        GridLayout gridLayout = new GridLayout(columns, false);
        group.setLayout(gridLayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.minimumHeight = minimumHeight;
        gridData.heightHint = minimumHeight;
        group.setLayoutData(gridData);
        return group;
    }

    /**
     * create startNewGrid (width Columns) in a Composite.
     * 
     * @param Composite parent
     * @param int numColums
     * @param boolean sameWidth
     * 
     * @return Composite
     */
    public static Composite startNewGridLayout(Composite parent, int numColums) {
        return startNewGridLayout(parent, numColums, false, SWT.FILL, SWT.FILL);
    }

    /**
     * create startNewGrid (width Columns) in a Composite.
     * 
     * @param Composite parent
     * @param int numColumns
     * @param boolean sameWidth
     * @param int styleHoryzontal
     * @param int styleVertical
     * 
     * @return Composite
     */
    public static Composite startNewGridLayout(Composite parent, int numColumns, boolean sameWidth, int styleHoryzontal,
            int styleVertical) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout(numColumns, sameWidth));
        composite.setLayoutData(new GridData(styleHoryzontal, styleVertical, true, true));
        return composite;
    }

    /**
     * create Dimensionned NewGrid (width Columns) in a Composite.
     * 
     * @param Composite parent
     * @param int numColumns
     * @param boolean sameWidth
     * @param int styleHoryzontal
     * @param int styleVertical
     * 
     * @return Composite
     */
    public static Composite startNewDimensionnedGridLayout(Composite parent, int numColumns, int minimumWidth, int minimumHeight) {

        return startNewDimensionnedGridLayout(parent, numColumns, minimumWidth, minimumHeight, true);

    }

    /**
     * create Dimensionned NewGrid (width Columns) in a Composite.
     * 
     * @param Composite parent
     * @param int numColumns
     * @param boolean sameWidth
     * @param int styleHoryzontal
     * @param int styleVertical
     * @param boolean isFillBoth
     * 
     * @return Composite
     */
    public static Composite startNewDimensionnedGridLayout(Composite parent, int numColumns, int minimumWidth, int minimumHeight,
            boolean isFillBoth) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout(numColumns, false));
        GridData gridData = isFillBoth ? new GridData(GridData.FILL_BOTH) : new GridData();
        gridData.minimumWidth = minimumWidth;
        gridData.minimumHeight = minimumHeight;
        gridData.widthHint = minimumWidth;
        gridData.heightHint = minimumHeight;
        composite.setLayoutData(gridData);
        return composite;
    }

    /**
     * create Horizontal Line in a Composite.
     * 
     * @param Composite parent
     * 
     */
    public void createHorizontalLine(Composite parent) {
        Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        line.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        return;
    }

}
