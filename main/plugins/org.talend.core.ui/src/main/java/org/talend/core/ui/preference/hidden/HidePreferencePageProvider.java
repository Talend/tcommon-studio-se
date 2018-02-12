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
package org.talend.core.ui.preference.hidden;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class HidePreferencePageProvider {

    private String name, desc, prefPageId;

    private IHidePreferencePageValidator validator;

    /**
     * DOC ggu HidePreferencePageProvider constructor comment.
     */
    public HidePreferencePageProvider() {
        super();
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for desc.
     * 
     * @return the desc
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * Sets the desc.
     * 
     * @param desc the desc to set
     */
    void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Getter for prefPageId.
     * 
     * @return the prefPageId
     */
    public String getPrefPageId() {
        return this.prefPageId;
    }

    /**
     * Sets the prefPageId.
     * 
     * @param prefPageId the prefPageId to set
     */
    void setPrefPageId(String prefPageId) {
        this.prefPageId = prefPageId;
    }

    /**
     * Getter for validator.
     * 
     * @return the validator if the validator is null, should be same as the the validator to retrurn true. and hide the
     * preference page
     */
    public IHidePreferencePageValidator getValidator() {
        return this.validator;
    }

    /**
     * Sets the validator.
     * 
     * @param validator the validator to set
     */
    void setValidator(IHidePreferencePageValidator validator) {
        this.validator = validator;
    }

}
