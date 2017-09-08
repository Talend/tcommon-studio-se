// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.commons.ui.swt.tableviewer;

import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class TableViewerCreatorColumn<B, V> extends TableViewerCreatorColumnNotModifiable<B, V> {

    /**
     * 
     * @param tableViewerCreator
     */
    public TableViewerCreatorColumn(TableViewerCreator<B> tableViewerCreator) {
        super(tableViewerCreator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn#getTableViewerCreator()
     */
    @Override
    public TableViewerCreator getTableViewerCreator() {
        return (TableViewerCreator<B>) super.getTableViewerCreator();
    }
}
