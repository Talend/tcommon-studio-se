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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * RegleTest
 * 
 * $Id: RegleTest.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */

public class RegleTest extends Composite {

    private static RegleTest composite;

    private Canvas canvas;

    public RegleTest(Composite parent, int style) {
        super(parent, style);

        composite = this;
        canvas = new Canvas(parent, SWT.NONE);
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;

                gc.drawLine(0, 20, 500, 20);

                int unite = 0;
                int uniteRAZ = 0;
                for (int i = 0; i < 500; i++) {
                    if (i % 9 == 0) {
                        if (uniteRAZ == 5) {
                            String num = "" + unite; //$NON-NLS-1$
                            if (num.length() < 2) {
                                gc.drawText(num, i - 2, 2);
                            } else if (num.length() < 3) {
                                gc.drawText(num, i - 5, 2);
                            } else if (num.length() < 4) {
                                gc.drawText(num, i - 8, 2);
                            } else {
                                gc.drawText(num, i - 11, 2);
                            }
                            gc.drawLine(i, 16, i, 24);
                            uniteRAZ = 0;
                        } else {
                            gc.drawLine(i, 18, i, 22);
                        }
                        uniteRAZ += 1;
                        unite += 1;
                    }
                }
                gc.dispose();
            }
        });
    }
}
