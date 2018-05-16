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
package org.talend.core.ui.context.nattableTree;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.painter.IOverlayPainter;
import org.eclipse.swt.graphics.GC;

/**
 * this one for caculate the size of the columns for the context table
 * 
 */
public class ContextTreeResizePainter implements IOverlayPainter {

    private NatTable table;

    private DataLayer mixDataLayer;

    private List<Integer> hidePoss;

    List<Integer> checkColumnPos;

    public ContextTreeResizePainter(NatTable tb, DataLayer dataLayer, List<Integer> hidePos, List<Integer> checkColumnPos) {
        table = tb;
        this.mixDataLayer = dataLayer;
        this.hidePoss = hidePos;
        this.checkColumnPos = checkColumnPos;
    }

    @Override
    public synchronized void paintOverlay(GC gc, ILayer layer) {

    }
}
