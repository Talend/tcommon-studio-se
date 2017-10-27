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
package org.talend.core.ui.perspective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.utils.RegistryReader;

/**
 * created by ggu on Nov 10, 2014 Detailled comment
 *
 */
public class ShowPerspectivesAtStartupRegistryReader extends RegistryReader {

    /**
     * key is the id of perspective, value are the id of perspectives for argument "appearsAfter".
     * 
     * The "appearsAfter" can be multi, split by "|".
     * 
     */
    private Map<String, List<String>> showPerspRelationMap = new HashMap<String, List<String>>();

    private String[] showPerspIds;

    protected ShowPerspectivesAtStartupRegistryReader() {
        super(CoreUIPlugin.PLUGIN_ID, "showPerspectiveAtStartup"); //$NON-NLS-1$
    }

    public void init() {
        readRegistry();

        // set up the arg for "appearsAfter"
        List<String> showPerspList = new ArrayList<String>();
        // only record the first valid perspective
        Map<String, String> validAppearsAfterMap = new HashMap<String, String>();

        for (String id : showPerspRelationMap.keySet()) {
            if (!validPerspective(id)) { // not existed in extension point.
                continue;
            }
            List<String> appearsAfterIds = showPerspRelationMap.get(id);
            if (appearsAfterIds == null || appearsAfterIds.isEmpty()) { // no "appearsAfter" argument
                showPerspList.add(id); // just add in the end of list.
            } else {
                String foundId = null; // only for first one
                for (String appearsAfterId : appearsAfterIds) {
                    if (showPerspRelationMap.containsKey(appearsAfterId) // found the define
                            && validPerspective(id)) { // existed in extension point.
                        foundId = appearsAfterId;
                        break;
                    }
                }
                if (foundId == null) { // don't existed the id for "appearsAfter".
                    showPerspList.add(id); // just add in the end of list.
                } else {
                    validAppearsAfterMap.put(id, foundId); // record
                }
            }

        }

        while (!validAppearsAfterMap.isEmpty()) {
            Set<String> done = new HashSet<>();
            // add the id with "appearsAfter" arg
            for (String id : validAppearsAfterMap.keySet()) {
                if (showPerspList.contains(id)) {
                    // in fact, shouldn't contain
                    done.add(id);
                } else {
                    String appearsAfterId = validAppearsAfterMap.get(id);
                    if (appearsAfterId != null) {
                        // find the index of "appearsAfter"
                        int index = showPerspList.indexOf(appearsAfterId);
                        if (index > -1) { // found
                            if (index < showPerspList.size()) {
                                showPerspList.add(index + 1, id);
                            } else {
                                showPerspList.add(id);
                            }
                            done.add(id);
                        }

                    }
                }
            }
            for (String id : done) {
                validAppearsAfterMap.remove(id);
            }
        }
        this.showPerspIds = showPerspList.toArray(new String[0]);
    }

    /**
     * 
     * DOC ggu Comment method "validPerspective".
     * 
     * check the perspective is real existed in extension point or not.
     * 
     * @param perspId
     * @return
     */
    private boolean validPerspective(String perspId) {
        if (perspId != null && !perspId.isEmpty()) {
            final IWorkbench workbench = PlatformUI.getWorkbench();
            IPerspectiveDescriptor perspDesc = workbench.getPerspectiveRegistry().findPerspectiveWithId(perspId);
            return perspDesc != null;
        }
        return false;
    }

    public String[] getShowPerspectiveIds() {
        if (this.showPerspIds == null) {
            return new String[0];
        }
        return this.showPerspIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if ("perspective".equals(element.getName())) { //$NON-NLS-1$
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    String id = element.getAttribute("id"); //$NON-NLS-1$
                    String appearsAfterIds = element.getAttribute("appearsAfter"); //$NON-NLS-1$

                    if (appearsAfterIds == null || appearsAfterIds.trim().length() == 0) {
                        showPerspRelationMap.put(id, null);
                    } else { // have value.
                        List<String> appearsAfterIdList = new ArrayList<String>();
                        //
                        String[] appearsAfterIdArray = appearsAfterIds.split("\\|"); //$NON-NLS-1$
                        if (appearsAfterIdArray != null) {
                            for (String appearsAfterId : appearsAfterIdArray) {
                                appearsAfterId = appearsAfterId.trim();
                                if (!appearsAfterId.isEmpty()) {
                                    appearsAfterIdList.add(appearsAfterId);
                                }
                            }
                        }
                        showPerspRelationMap.put(id, appearsAfterIdList);
                    }
                }
            });
            return true;
        }
        return false;
    }
}
