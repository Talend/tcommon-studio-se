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
package org.talend.core.repository.seeker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.utils.RegistryReader;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
@SuppressWarnings("rawtypes")
public final class RepositorySeekerRegistryReader extends RegistryReader {

    private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

    private static final String REPO_SEEKER_ELEMENT_NAME = "seeker"; //$NON-NLS-1$

    private static final String REPO_SEEKER_EXTENSION_POINT = "repositorySeeker"; //$NON-NLS-1$

    private List<IRepositorySeeker> allSeeker;

    public RepositorySeekerRegistryReader() {
        super(FrameworkUtil.getBundle(RepositorySeekerRegistryReader.class).getSymbolicName(), REPO_SEEKER_EXTENSION_POINT);
    }

    public IRepositorySeeker<IRepositoryNode>[] getAllSeeker() {
        if (this.allSeeker == null) {
            this.allSeeker = new ArrayList<IRepositorySeeker>();
            readRegistry();
        }
        //
        return this.allSeeker.toArray(new IRepositorySeeker[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if (REPO_SEEKER_ELEMENT_NAME.equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    IRepositorySeeker<?> seeker = (IRepositorySeeker<?>) element.createExecutableExtension(CLASS_ATTRIBUTE);
                    allSeeker.add(seeker);

                }
            });
            return true;
        }
        return false;
    }

}
