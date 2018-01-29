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

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.group.action.ColumnGroupExpandCollapseAction;
import org.eclipse.nebula.widgets.nattable.group.action.ColumnGroupHeaderReorderDragMode;
import org.eclipse.nebula.widgets.nattable.group.action.ColumnHeaderReorderDragMode;
import org.eclipse.nebula.widgets.nattable.group.action.CreateColumnGroupAction;
import org.eclipse.nebula.widgets.nattable.group.action.UngroupColumnsAction;
import org.eclipse.nebula.widgets.nattable.group.config.DefaultColumnGroupHeaderLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.group.painter.ColumnGroupHeaderTextPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.action.AggregateDragMode;
import org.eclipse.nebula.widgets.nattable.ui.action.CellDragMode;
import org.eclipse.nebula.widgets.nattable.ui.action.NoOpMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.KeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.swt.SWT;

/**
 * created by ldong on Sep 19, 2014 Detailled comment
 * 
 */
public class ContextColumnGroupConfiguration extends DefaultColumnGroupHeaderLayerConfiguration {

    private final ColumnGroupModel columnGroupModel;

    /**
     * DOC ldong ContextColumnGroupConfiguration constructor comment.
     * 
     * @param columnGroupModel
     */
    public ContextColumnGroupConfiguration(ColumnGroupModel columnGroupModel) {
        super(columnGroupModel);
        this.columnGroupModel = columnGroupModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.group.config.DefaultColumnGroupHeaderLayerConfiguration#configureRegistry
     * (org.eclipse.nebula.widgets.nattable.config.IConfigRegistry)
     */
    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new ContextColumnHeaderDecorator(
                new ColumnGroupHeaderTextPainter()), DisplayMode.NORMAL, GridRegion.COLUMN_GROUP_HEADER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.group.config.DefaultColumnGroupHeaderLayerConfiguration#configureUiBindings
     * (org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry)
     */
    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        uiBindingRegistry.registerMouseDragMode(MouseEventMatcher.columnGroupHeaderLeftClick(SWT.NONE), new AggregateDragMode(
                new CellDragMode(), new ColumnGroupHeaderReorderDragMode(columnGroupModel)));

        uiBindingRegistry.registerMouseDragMode(MouseEventMatcher.columnHeaderLeftClick(SWT.NONE),
                new ColumnHeaderReorderDragMode(columnGroupModel));

        uiBindingRegistry.registerDoubleClickBinding(MouseEventMatcher.columnGroupHeaderLeftClick(SWT.NONE),
                new NoOpMouseAction());
        // just switch the action for the double click and single click
        uiBindingRegistry.registerFirstSingleClickBinding(MouseEventMatcher.columnGroupHeaderLeftClick(SWT.NONE),
                new ColumnGroupExpandCollapseAction());

        uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.CTRL, 'g'), new CreateColumnGroupAction());
        uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.CTRL, 'u'), new UngroupColumnsAction());
    }

}
