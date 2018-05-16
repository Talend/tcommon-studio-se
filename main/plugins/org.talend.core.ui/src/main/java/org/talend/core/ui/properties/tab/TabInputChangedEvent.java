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
package org.talend.core.ui.properties.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.views.properties.tabbed.ITabItem;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class TabInputChangedEvent {

    private ITabItem tabItem;

    private Object[] newInput;

    private IStructuredSelection selection;

    /**
     * yzhang TabInputChangedEvent constructor comment.
     */
    public TabInputChangedEvent(ITabItem selectedTab, Object[] newInput) {
        this.tabItem = selectedTab;

        this.newInput = newInput;

        selection = new IStructuredSelection() {

            public Object getFirstElement() {
                return null;
            }

            public Iterator iterator() {
                return null;
            }

            public int size() {
                return 0;
            }

            public Object[] toArray() {
                return null;
            }

            public List toList() {
                List<ITabItem> d = new ArrayList<ITabItem>();
                if (tabItem != null) {
                    d.add(tabItem);
                }
                return d;
            }

            public boolean isEmpty() {
                return false;
            }

        };

    }

    /**
     * yzhang Comment method "getNewInput".
     * 
     * @return
     */
    public Object[] getInput() {
        return newInput;
    }

    /**
     * yzhang Comment method "getDefaultSelection".
     * 
     * @return
     */
    public IStructuredSelection getDefaultSelection() {
        return selection;
    }

}
