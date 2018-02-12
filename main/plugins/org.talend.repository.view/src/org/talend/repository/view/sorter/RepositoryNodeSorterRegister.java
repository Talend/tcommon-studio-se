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
package org.talend.repository.view.sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.talend.core.utils.RegistryReader;
import org.talend.repository.RepositoryViewPlugin;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class RepositoryNodeSorterRegister extends RegistryReader {

    private static final Logger log = Logger.getLogger(RepositoryNodeSorterRegister.class);

    class RepositoryNodeSorterBean {

        String name, desc, id, overrideId;

        IRepositoryNodeSorter sorter;

        int order;
    }

    private static RepositoryNodeSorterRegister INSTANCE;

    private List<RepositoryNodeSorterBean> sorterBeans = new ArrayList<RepositoryNodeSorterBean>();

    private IRepositoryNodeSorter[] finalSorters;

    public static RepositoryNodeSorterRegister getInstance() {
        if (INSTANCE == null) {
            synchronized (RepositoryNodeSorterRegister.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryNodeSorterRegister();
                    INSTANCE.init();
                }
            }
        }
        return INSTANCE;
    }

    private RepositoryNodeSorterRegister() {
        super(RepositoryViewPlugin.PLUGIN_ID, "repositorySorter"); //$NON-NLS-1$
    }

    private void init() {
        readRegistry();

        // process override ids
        List<String> overrideIds = new ArrayList<String>();
        for (RepositoryNodeSorterBean b : sorterBeans) {
            if (b.overrideId != null && b.overrideId.length() > 0) {
                overrideIds.add(b.overrideId);
            }
        }

        finalSorters = sorterBeans.stream().filter(b -> !overrideIds.contains(b.id))
                .sorted(new Comparator<RepositoryNodeSorterBean>() {

                    @Override
                    public int compare(RepositoryNodeSorterBean o1, RepositoryNodeSorterBean o2) {
                        return o1.order - o2.order;
                    }
                }).map(b -> b.sorter).toArray(IRepositoryNodeSorter[]::new);

    }

    public IRepositoryNodeSorter[] getSorters() {
        if (finalSorters == null) {
            return new IRepositoryNodeSorter[0];
        }
        return finalSorters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(IConfigurationElement element) {
        if ("sorter".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    String name = element.getAttribute("name"); //$NON-NLS-1$
                    String description = element.getAttribute("description"); //$NON-NLS-1$
                    String id = element.getAttribute("id"); //$NON-NLS-1$
                    if (id == null || id.length() == 0) {
                        log.error("The id shouldn't be empty to set in bundle" + element.getContributor().getName());
                        return;
                    }

                    String orderStr = element.getAttribute("order"); //$NON-NLS-1$
                    int order = 0;
                    try {
                        if (orderStr != null && orderStr.length() > 0) {
                            order = Integer.parseInt(orderStr);
                        }
                    } catch (NumberFormatException e) {
                        log.error("Must set the order to be integer for " + id + " in bundle"
                                + element.getContributor().getName());
                        return;
                    }
                    String overrideId = element.getAttribute("override"); //$NON-NLS-1$
                    IRepositoryNodeSorter sorter = null;
                    try {
                        sorter = (IRepositoryNodeSorter) element.createExecutableExtension("class");
                    } catch (CoreException e) {
                        log.error("Create sorter instance failure", e);
                    }
                    if (sorter == null) {
                        log.error("Can't create the sorter for " + id + " in bundle " + element.getContributor().getName());
                        return;
                    }

                    RepositoryNodeSorterBean bean = new RepositoryNodeSorterBean();
                    bean.name = name;
                    bean.id = id;
                    bean.desc = description;
                    bean.order = order;
                    bean.overrideId = overrideId;
                    bean.sorter = sorter;

                    sorterBeans.add(bean);
                }
            });
            return true;
        }
        return false;
    }
}
