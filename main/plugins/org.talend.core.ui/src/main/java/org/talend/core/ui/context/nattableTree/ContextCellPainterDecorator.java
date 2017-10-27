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
package org.talend.core.ui.context.nattableTree;

import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;

/**
 * created by ldong on Jul 31, 2014 Detailled comment
 * 
 */
class ContextCellPainterDecorator extends CellPainterDecorator {

    private ContextAutoResizeTextPainter baseCellPainter;

    private ICellPainter decoratorCellPainter;

    private boolean isChange;

    /**
     * DOC Talend ContextCellPainterDecorator constructor comment.
     * 
     * @param baseCellPainter
     * @param cellEdge
     * @param decoratorCellPainter
     */
    public ContextCellPainterDecorator(ContextAutoResizeTextPainter baseCellPainter, CellEdgeEnum cellEdge,
            ICellPainter decoratorCellPainter) {
        super(baseCellPainter, cellEdge, decoratorCellPainter);
        this.baseCellPainter = baseCellPainter;
        this.decoratorCellPainter = decoratorCellPainter;
    }

    public void setChangeBackgroundColor(boolean isChange) {
        this.isChange = isChange;
        baseCellPainter.setChangeBackgroundColor(isChange);
    }
}
