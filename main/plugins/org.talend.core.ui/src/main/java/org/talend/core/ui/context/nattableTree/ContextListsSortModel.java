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

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyResolver;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;

import ca.odell.glazedlists.EventList;

/**
 * created by ldong on Jul 14, 2014 Detailled comment
 * 
 */
public class ContextListsSortModel<T> implements ILayerListener {

    protected final EventList<T> eventList;

    protected final IColumnAccessor<T> columnAccessor;

    protected final IColumnPropertyResolver columnPropertyResolver;

    protected final IConfigRegistry configRegistry;

    protected final ILayer columnHeaderDataLayer;

    public ContextListsSortModel(EventList<T> eventList, IColumnPropertyAccessor<T> columnPropertyAccessor,
            IConfigRegistry configRegistry, ILayer dataLayer) {
        this(eventList, columnPropertyAccessor, columnPropertyAccessor, configRegistry, dataLayer);
    }

    public ContextListsSortModel(EventList<T> eventList, IColumnAccessor<T> columnAccessor,
            IColumnPropertyResolver columnPropertyResolver, IConfigRegistry configRegistry, ILayer dataLayer) {
        this.eventList = eventList;
        this.columnAccessor = columnAccessor;
        this.columnPropertyResolver = columnPropertyResolver;
        this.configRegistry = configRegistry;
        this.columnHeaderDataLayer = dataLayer;

        this.columnHeaderDataLayer.addLayerListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.nattable.layer.ILayerListener#handleLayerEvent(org.eclipse.nebula.widgets.nattable
     * .layer.event.ILayerEvent)
     */
    @Override
    public void handleLayerEvent(ILayerEvent arg0) {

    }

}
