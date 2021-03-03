// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.BackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.model.ContextTabChildModel;
import org.talend.core.ui.context.model.ContextTabParentModel;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;

/**
 * For the repository context's background.we control its backgroup colour is gray
 *
 */
public class ContextNatTableBackGroudPainter extends BackgroundPainter {

    private IDataProvider dataProvider;

    private Map<String, String> rowNames = new HashMap<String, String>();

    public ContextNatTableBackGroudPainter(ICellPainter painter, IDataProvider dataProvider) {
        super(painter);
        this.dataProvider = dataProvider;
    }

    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(cell.getRowIndex());
        if (rowNode.getTreeData() instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel rowModel = (ContextTableTabParentModel) rowNode.getTreeData();
            Boolean isRepositoryContext = rowModel.hasChildren();
            if (isRepositoryContext) {
                ((ContextAutoResizeTextPainter) getWrappedPainter()).setChangeBackgroundColor(true);
            } else {
                ((ContextAutoResizeTextPainter) getWrappedPainter()).setChangeBackgroundColor(false);
            }
        } else {
            ContextTabChildModel rowChildModel = (ContextTabChildModel) rowNode.getTreeData();
            if (rowChildModel != null) {
                ((ContextAutoResizeTextPainter) getWrappedPainter()).setChangeBackgroundColor(true);
                checkContainsRowName(rowNode, rowChildModel);
            }
        }
        super.paintCell(cell, gc, bounds, configRegistry);
        ((ContextAutoResizeTextPainter) getWrappedPainter()).setContainsRowName(false);
    }

    @Override
    protected Color getBackgroundColour(ILayerCell cell, IConfigRegistry configRegistry) {
        return super.getBackgroundColour(cell, configRegistry);
    }

    private void checkContainsRowName(ContextTreeNode rowNode, ContextTabChildModel rowChildModel) {
        // Check any duplicate / similar variable from different context to show in red .
        String rowName = rowNode.getName();
        String parentSourceName = null;
        ContextTabParentModel rowTabParentModel = rowChildModel.getParent();
        if (rowTabParentModel != null && rowTabParentModel instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel rowTableTabParentModel = (ContextTableTabParentModel) rowTabParentModel;
            parentSourceName = rowTableTabParentModel.getSourceName();
        }
        if (StringUtils.isNotBlank(rowName) && StringUtils.isNotBlank(parentSourceName)) {
            rowName = rowName.toUpperCase();
            parentSourceName = parentSourceName.toUpperCase();
            if (rowNames.containsValue(rowName)) {
                Iterator<String> iterator = rowNames.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = rowNames.get(key);
                    if (rowName.equalsIgnoreCase(value)) {
                        if (!parentSourceName.equalsIgnoreCase(key)) {
                            ((ContextAutoResizeTextPainter) getWrappedPainter()).setContainsRowName(true);
                        }
                        break;
                    }
                }
            } else {
                rowNames.put(parentSourceName, rowName);
            }
        }
    }
}
