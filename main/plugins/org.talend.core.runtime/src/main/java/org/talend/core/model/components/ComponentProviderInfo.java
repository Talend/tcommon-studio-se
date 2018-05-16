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
package org.talend.core.model.components;

/**
 * created by Talend on 2015年7月22日 Detailled comment
 *
 */
public class ComponentProviderInfo {

    private String id;

    private String contributer;

    private String location;

    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for contributer.
     * 
     * @return the contributer
     */
    public String getContributer() {
        return this.contributer;
    }

    /**
     * Sets the contributer.
     * 
     * @param contributer the contributer to set
     */
    public void setContributer(String contributer) {
        this.contributer = contributer;
    }

    /**
     * Getter for location.
     * 
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the location.
     * 
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

}
