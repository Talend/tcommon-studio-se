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
package org.talend.designer.runprocess;

import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public interface IProcessMessageManager {

    public void addPropertyChangeListener(PropertyChangeListener l);

    public void firePropertyChange(String property, Object oldValue, Object newValue);

    public void removePropertyChangeListener(PropertyChangeListener l);

    public void addMessage(IProcessMessage message);

    public void clearMessages();

    public void updateConsole();

    public Collection<IProcessMessage> getMessages();

    public boolean isLastMessageEndWithCR();

    public void addDebugResultToConsole(IProcessMessage message);

}
