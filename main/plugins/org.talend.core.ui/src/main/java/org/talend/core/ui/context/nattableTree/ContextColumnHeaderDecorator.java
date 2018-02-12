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

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * created by ldong on Sep 22, 2014 Detailled comment
 * 
 */
public class ContextColumnHeaderDecorator extends CellPainterWrapper {

    private boolean uplift = true;

    public ContextColumnHeaderDecorator(ICellPainter interiorPainter) {
        super(interiorPainter);
    }

    public ContextColumnHeaderDecorator(ICellPainter interiorPainter, boolean uplift) {
        super(interiorPainter);
        this.uplift = uplift;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator#paintCell(org.eclipse.nebula
     * .widgets.nattable.layer.cell.ILayerCell, org.eclipse.swt.graphics.GC, org.eclipse.swt.graphics.Rectangle,
     * org.eclipse.nebula.widgets.nattable.config.IConfigRegistry)
     */
    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle adjustedCellBounds, IConfigRegistry configRegistry) {
        Rectangle interiorBounds = getWrappedPainterBounds(cell, gc, adjustedCellBounds, configRegistry);
        super.paintCell(cell, gc, interiorBounds, configRegistry);

        // Save GC settings
        Color originalForeground = gc.getForeground();

        gc.setForeground(uplift ? GUIHelper.COLOR_WIDGET_LIGHT_SHADOW : GUIHelper.COLOR_WIDGET_DARK_SHADOW);
        gc.drawLine(adjustedCellBounds.x, adjustedCellBounds.y, adjustedCellBounds.x + adjustedCellBounds.width - 1,
                adjustedCellBounds.y);
        gc.drawLine(adjustedCellBounds.x, adjustedCellBounds.y, adjustedCellBounds.x, adjustedCellBounds.y
                + adjustedCellBounds.height - 1);

        // Down
        gc.setForeground(uplift ? GUIHelper.COLOR_WIDGET_DARK_SHADOW : GUIHelper.COLOR_WIDGET_LIGHT_SHADOW);
        gc.drawLine(adjustedCellBounds.x, adjustedCellBounds.y + adjustedCellBounds.height - 1, adjustedCellBounds.x
                + adjustedCellBounds.width - 1, adjustedCellBounds.y + adjustedCellBounds.height - 1);
        gc.drawLine(adjustedCellBounds.x + adjustedCellBounds.width - 1, adjustedCellBounds.y, adjustedCellBounds.x
                + adjustedCellBounds.width - 1, adjustedCellBounds.y + adjustedCellBounds.height - 1);

        // Restore GC settings
        gc.setForeground(originalForeground);
    }

}
