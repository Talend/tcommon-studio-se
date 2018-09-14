package org.talend.repository.ui.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.repository.model.RepositoryNode;

public class ActionFilterDelegateFactory {

    private static final String EXTENSION_ID = "org.talend.core.runtime.actionFilterDelegate"; //$NON-NLS-1$
    
    private static ActionFilterDelegateFactory instance;


    private Map<String, IActionFilterDelegate> actionFilterDelegateContributionsMap;
    
    
    public ActionFilterDelegateFactory() {
        super();
        actionFilterDelegateContributionsMap = new HashMap<String, IActionFilterDelegate>();
        IConfigurationElement[] contribs = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
        
        for (IConfigurationElement element : contribs) {
            
            try {
                final String id = element.getAttribute("id");
                final IActionFilterDelegate actionFilterDelegate = (IActionFilterDelegate) element.createExecutableExtension("class");
                actionFilterDelegateContributionsMap.put(id, actionFilterDelegate);
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
    }




    public static ActionFilterDelegateFactory getInstance() {
        if (instance == null) {
            instance = new ActionFilterDelegateFactory();
        }
        return instance;
    }
    
    
    public boolean testAttribute(String value, RepositoryNode node) {
        IActionFilterDelegate actionFilterDelegate = actionFilterDelegateContributionsMap.get(value);
        if (actionFilterDelegate != null) {
            return actionFilterDelegate.testAttribute(node, value);
        }
        return false;
    }
    
    
}
