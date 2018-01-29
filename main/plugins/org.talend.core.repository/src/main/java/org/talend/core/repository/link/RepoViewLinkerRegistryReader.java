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
package org.talend.core.repository.link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.utils.RegistryReader;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class RepoViewLinkerRegistryReader extends RegistryReader {

    static class LinkerRecorder {

        enum Priority {
            LOWEST,
            LOW,
            NORMAL,
            HIGH,
            HIGHEST
        }

        public final Priority priority;

        public final IRepoViewLinker linker;

        public LinkerRecorder(IRepoViewLinker l, Priority p) {
            super();
            this.linker = l;
            this.priority = p;
        }

    }

    private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

    private static final String PRIORITY_ATTRIBUTE = "priority"; //$NON-NLS-1$

    private static final String VIEW_LINKER_ELEMENT_NAME = "linker"; //$NON-NLS-1$

    private static final String VIEW_LINKER_EXTENSION_POINT = "repoViewLinker"; //$NON-NLS-1$

    private List<LinkerRecorder> allLinkerRecorder;

    private static RepoViewLinkerRegistryReader linkRegReader;

    private RepoViewLinkerRegistryReader() {
        super(FrameworkUtil.getBundle(RepoViewLinkerRegistryReader.class).getSymbolicName(), VIEW_LINKER_EXTENSION_POINT);
    }

    public static RepoViewLinkerRegistryReader getInstance() {
        if (linkRegReader == null) {
            linkRegReader = new RepoViewLinkerRegistryReader();
        }
        return linkRegReader;
    }

    public synchronized IRepoViewLinker[] getAllRepoViewLinkers() {
        if (this.allLinkerRecorder == null) {
            this.allLinkerRecorder = new ArrayList<LinkerRecorder>();
            readRegistry();

            // sort
            Collections.sort(this.allLinkerRecorder, new Comparator<LinkerRecorder>() {

                @Override
                public int compare(LinkerRecorder lr1, LinkerRecorder lr2) {
                    return lr2.priority.compareTo(lr1.priority);
                }
            });
        }
        //
        IRepoViewLinker[] linkers = new IRepoViewLinker[this.allLinkerRecorder.size()];
        int index = 0;
        for (LinkerRecorder recorder : this.allLinkerRecorder) {
            linkers[index++] = recorder.linker;
        }
        return linkers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if (VIEW_LINKER_ELEMENT_NAME.equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    IRepoViewLinker linker = (IRepoViewLinker) element.createExecutableExtension(CLASS_ATTRIBUTE);
                    String priorityString = element.getAttribute(PRIORITY_ATTRIBUTE);
                    LinkerRecorder.Priority priority = (priorityString != null && priorityString.length() > 0) ? LinkerRecorder.Priority
                            .valueOf(priorityString.toUpperCase()) : LinkerRecorder.Priority.NORMAL;
                    allLinkerRecorder.add(new LinkerRecorder(linker, priority));

                }
            });
            return true;
        }
        return false;
    }
}
