// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.components;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;

public class IODataComponentContainer {

    private List<IODataComponent> inputs;

    private List<IODataComponent> ouputs;

    public IODataComponentContainer() {
        this.inputs = new ArrayList<IODataComponent>();
        this.ouputs = new ArrayList<IODataComponent>();
    }

    public List<IODataComponent> getInputs() {
        return this.inputs;
    }

    public List<IODataComponent> getOuputs() {
        return this.ouputs;
    }

    public IODataComponent getDataComponent(IConnection connection) {
        for (IODataComponent current : inputs) {
            if (current.getUniqueName().equals(connection.getUniqueName())
                    && current.getConnection().getSource().getUniqueName().equals(connection.getSource().getUniqueName())
                    && current.getConnection().getTarget().getUniqueName().equals(connection.getTarget().getUniqueName())) {
                return current;
            }
        }
        for (IODataComponent current : ouputs) {
            if (current.getUniqueName().equals(connection.getUniqueName())
                    && current.getConnection().getSource().getUniqueName().equals(connection.getSource().getUniqueName())
                    && current.getConnection().getTarget().getUniqueName().equals(connection.getTarget().getUniqueName())) {
                return current;
            }
        }
        return null;
    }

    public IMetadataTable getTable(IConnection connection) {
        IODataComponent current = getDataComponent(connection);
        if (current == null) {
            return null;
        } else {
            return current.getTable();
        }
    }
}
