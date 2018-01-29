package org.talend.core.ui.services;

import java.io.Serializable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.IService;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.properties.tab.IDynamicProperty;

public interface IGitUIProviderService extends IService {

    boolean isGitHistoryComposite(IDynamicProperty dp);

    ISelection getGitHistorySelection(IDynamicProperty dp);

    IDynamicProperty createProcessGitHistoryComposite(Composite parent, Object view, TabbedPropertySheetWidgetFactory factory,
            IRepositoryViewObject obj);

    public String[] changeCredentials(Shell parent, Serializable uriIsh, String initUser, boolean canStoreCredentials);

}
