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
package org.talend.repository.viewer.content.listener;


/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class TopLevelNodeRunnable implements Runnable {

    private final Object topLevelNode;

    public TopLevelNodeRunnable(Object node) {
        super();
        this.topLevelNode = node;
    }

    public Object getTopLevelNode() {
        return this.topLevelNode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.topLevelNode == null) ? 0 : this.topLevelNode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TopLevelNodeRunnable other = (TopLevelNodeRunnable) obj;
        if (this.topLevelNode == null) {
            if (other.topLevelNode != null) {
                return false;
            }
        } else if (!this.topLevelNode.equals(other.topLevelNode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTopLevelNode().toString();
    }

    @Override
    public abstract void run();

}
