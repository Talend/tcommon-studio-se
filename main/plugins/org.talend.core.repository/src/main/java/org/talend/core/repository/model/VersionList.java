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
package org.talend.core.repository.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * Used to manage IRepositoryObject list with allowing or not of all versions of each object. Constructor parameter
 * <code>allVersion</code> specify, if <code>false</code>, that only the most recent version of an object must be kept.
 * This functionnality is realized by the <code>add</code> method.<br/>
 * 
 * PTODO SML Overide other way to put object in the list
 * 
 * $Id: VersionList.java 44053 2010-06-12 09:14:16Z nma $
 * 
 */
public class VersionList extends ArrayList<IRepositoryViewObject> {

    private static final long serialVersionUID = 6286880354826726354L;

    private boolean allVersion;

    /**
     * The key is IRepositoryObject.getId(), the value is index of IRepositoryObject in arraylist.
     */
    private Map<String, Integer> idToIndexMap = new HashMap<String, Integer>();

    /**
     * DEfault constructor.
     * 
     * @param allVersion - <code>true</code> if all versions of each object must be kept, <code>false</code> otherwise.
     */
    public VersionList(boolean allVersion) {
        super();
        this.allVersion = allVersion;
    }

    /**
     * Add object in the list following the versionning strategy specify by the <code>allVersion</code> field.<br/>
     * 
     * If <code>false</code>, method will add object only if
     * <ul>
     * <li>no object with this id could be found or</li>
     * <li>an object with this id can be found and new object version is most recent than old object. Then old is
     * removed and new is added.</li>
     * </ul>
     * So only the most recent version of the object is kept.
     * 
     * @param o - the object to add
     * @return see Collection.add contract
     * @see Collection.add
     */
    @Override
    public boolean add(IRepositoryViewObject o) {
        if (allVersion) {
            return super.add(o);
        } else {
            Integer index = idToIndexMap.get(o.getId());
            if (index == null) {
                // no object with same id found, add the object to arrayList, add the index of new object to map.
                idToIndexMap.put(o.getId(), super.size());
                return super.add(o);
            } else {
                IRepositoryViewObject another = super.get(index);
                // compare version, see bug 0004190: Version check for the latest version is wrong.
                if (VersionUtils.compareTo(o.getVersion(), another.getVersion()) > 0) {
                    // version is newer than existing one , replace it
                    super.set(index, o);
                    return true;
                } else {
                    // version is older than existing one
                    return false;
                }
            }

        }
    }

}
