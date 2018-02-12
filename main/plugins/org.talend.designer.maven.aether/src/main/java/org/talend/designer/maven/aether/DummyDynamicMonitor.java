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
package org.talend.designer.maven.aether;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DummyDynamicMonitor implements IDynamicMonitor {

    @Override
    public void writeMessage(String message) {
        // nothing to do
    }

    @Override
    public void beginTask(String name, int totalWork) {
        // TODO Auto-generated method stub

    }

    @Override
    public void done() {
        // TODO Auto-generated method stub

    }

    @Override
    public void internalWorked(double work) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCanceled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setCanceled(boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTaskName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void subTask(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void worked(int work) {
        // TODO Auto-generated method stub

    }

}
