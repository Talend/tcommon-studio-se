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
package org.talend.core.ui.token;

/**
 * ggu class global comment. Detailled comment
 */
public class TokenInforProvider {

    private String id, name, description, productType;

    private ITokenCollector collector;

    public TokenInforProvider(String id, String name, String description, String productType, ITokenCollector collector) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.collector = collector;
    }

    public String getProductType() {
        return productType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ITokenCollector getCollector() {
        return collector;
    }

    @Override
    public int hashCode() {
        return 13 * id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TokenInforProvider)) {
            return false;
        }
        TokenInforProvider other = (TokenInforProvider) obj;

        return other.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return "id: " + this.getId() + "\n name:" + this.getName() + "\n product:" + this.getProductType();
    }

}
