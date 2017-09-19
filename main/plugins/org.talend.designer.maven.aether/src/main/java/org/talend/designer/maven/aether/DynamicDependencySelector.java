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
package org.talend.designer.maven.aether;

import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDependencySelector implements DependencySelector {

    private DependencySelector proxy;

    private DynamicDependencySelector parentSelector;

    private DependencyCollectionContext context;

    private IDynamicMonitor monitor;

    public DependencySelector getProxy() {
        return this.proxy;
    }

    public void setProxy(DependencySelector proxy) {
        this.proxy = proxy;
    }

    public DynamicDependencySelector getParentSelector() {
        return this.parentSelector;
    }

    public void setParentSelector(DynamicDependencySelector parentSelector) {
        this.parentSelector = parentSelector;
    }

    @Override
    public boolean selectDependency(Dependency dependency) {
        boolean result = proxy.selectDependency(dependency);
        if (monitor != null) {
            try {
                String message = "";
                if (result) {
                    message = "Collect: ";
                } else {
                    message = "Ignore: ";
                }
                message = message + dependency.toString();
                monitor.writeMessage(message + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public DependencyCollectionContext getContext() {
        return this.context;
    }

    public void setContext(DependencyCollectionContext context) {
        this.context = context;
    }

    public IDynamicMonitor getMonitor() {
        return this.monitor;
    }

    public void setMonitor(IDynamicMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
        DependencySelector result = proxy.deriveChildSelector(context);
        // System.out.println("deriveChildSelector: " + context.toString());
        DynamicDependencySelector selector = new DynamicDependencySelector();
        selector.setParentSelector(this);
        selector.setProxy(result);
        selector.setContext(context);
        selector.setMonitor(monitor);

        if (monitor != null) {
            try {
                String message = "\n=== Collect dependencies for " + context.getDependency().toString() + " ===";
                monitor.writeMessage(message + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return selector;
    }

    public String getPath() {
        StringBuffer buffer = new StringBuffer();

        DynamicDependencySelector pSelector = getParentSelector();
        if (pSelector != null) {
            String path = pSelector.getPath();
            buffer.append(path).append(" > ");
        }

        if (context != null) {
            buffer.append(context.toString());
        }

        return buffer.toString();
    }

}
